package com.woowahan.android10.deliverbanchan.presentation.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.google.gson.Gson
import com.woowahan.android10.deliverbanchan.data.local.background.LocalDBWorker
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.model.UiCartJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiRecentlyJoinItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import com.woowahan.android10.deliverbanchan.domain.usecase.DeleteCartInfoByHashUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetJoinUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.InsertCartInfoUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.InsertOrderInfoUseCase
import com.woowahan.android10.deliverbanchan.presentation.cart.model.TempOrder
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartBottomBody
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartHeader
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getJoinUseCase: GetJoinUseCase,
    private val deleteCartInfoByHashUseCase: DeleteCartInfoByHashUseCase,
    private val insertOrderInfoUseCase: InsertOrderInfoUseCase,
    private val cartRepository: CartRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    application: Application
) : ViewModel() {

    companion object {
        const val TAG = "CartViewModel"
    }

    private val workManager = WorkManager.getInstance(application)

    val appBarTitle = MutableLiveData("")
    val orderDetailMode = MutableLiveData(false)

    private val _allCartJoinState =
        MutableStateFlow<UiLocalState<UiCartJoinItem>>(UiLocalState.Init)
    val allCartJoinState: StateFlow<UiLocalState<UiCartJoinItem>>
        get() = _allCartJoinState.stateIn(
            initialValue = UiLocalState.Init,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )
    private val _allRecentlyJoinState =
        MutableStateFlow<UiLocalState<UiRecentlyJoinItem>>(UiLocalState.Init)
    val allRecentlyJoinState: StateFlow<UiLocalState<UiRecentlyJoinItem>>
        get() = _allRecentlyJoinState.stateIn(
            initialValue = UiLocalState.Init,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private var _itemCartBottomBodyProductTotalPrice = 0

    private val _itemCartHeaderData = MutableLiveData(UiCartHeader.emptyItem())
    val itemCartHeaderData: LiveData<UiCartHeader> get() = _itemCartHeaderData

    private val _uiCartJoinList = MutableLiveData<List<UiCartJoinItem>>(emptyList())
    val uiCartJoinList: LiveData<List<UiCartJoinItem>> get() = _uiCartJoinList
    private val _uiCartJoinArrayList = ArrayList<UiCartJoinItem>()

    private val _itemCartBottomBodyData = MutableLiveData(UiCartBottomBody.emptyItem())
    val itemCartBottomBodyData: LiveData<UiCartBottomBody> get() = _itemCartBottomBodyData

    private val _selectedCartItem = mutableSetOf<TempOrder>()

    private val _toBeDeletedCartItem = mutableSetOf<String>()


    init {
        getAllRecentlyJoinList()
        getAllCartJoinList()
    }

    internal fun updateCartDataBase() {
        val request = OneTimeWorkRequestBuilder<LocalDBWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(createInputData())
            .build()

        workManager.enqueue(request)
    }

    private fun createInputData(): Data {
        val builder = Data.Builder()

        val cartList = _uiCartJoinList.value!!.map {
            CartInfo(
                it.hash,
                it.checked,
                it.amount
            )
        }.toList()

        val deleteList = _toBeDeletedCartItem.toList()

        val gson = Gson()
        val cartListStr = gson.toJson(cartList)
        //val deleteListStr = gson.toJson(deleteList)

        builder.putString("cartListStr", cartListStr)
        builder.putStringArray("deleteListStr", deleteList.toTypedArray())
        return builder.build()
    }

    private fun getAllCartJoinList() = viewModelScope.launch {
        getJoinUseCase.getCartJoinList().onStart {
            _allCartJoinState.value = UiLocalState.IsLoading(true)
        }.flowOn(dispatcher).catch { exception ->
            _allCartJoinState.value = UiLocalState.IsLoading(false)
            _allCartJoinState.value = UiLocalState.ShowToast(exception.message.toString())
        }.onEach { uiCartJoinItemList ->
            calcCartBottomBodyAndHeaderVal(uiCartJoinItemList)
        }.collect {
            _allCartJoinState.value = UiLocalState.IsLoading(false)
            _allCartJoinState.value = UiLocalState.Success(it)
            _uiCartJoinArrayList.clear()
            _uiCartJoinArrayList.addAll(it)
            _uiCartJoinList.value = _uiCartJoinArrayList
        }
    }

    fun calcCartBottomBodyAndHeaderVal(uiCartJoinItemList: List<UiCartJoinItem>) {
        _itemCartBottomBodyProductTotalPrice = 0
        _selectedCartItem.clear()
        val checkedUiJoinCartItem = uiCartJoinItemList.filter { it.checked }
        checkedUiJoinCartItem.forEach { checkedUiCartJoinItem ->
            _selectedCartItem.add(TempOrder(checkedUiCartJoinItem.hash, checkedUiCartJoinItem.amount))
            _itemCartBottomBodyProductTotalPrice += checkedUiCartJoinItem.totalPrice
        }
        if (checkedUiJoinCartItem.size == uiCartJoinItemList.size) {
            _itemCartHeaderData.value = UiCartHeader(
                checkBoxText = UiCartHeader.TEXT_SELECT_RELEASE,
                checkBoxChecked = true
            )
        } else {
            _itemCartHeaderData.value = UiCartHeader(
                checkBoxText = UiCartHeader.TEXT_SELECT_ALL,
                checkBoxChecked = false
            )
        }
        setItemCartBottomBodyData()
    }

    private fun setItemCartBottomBodyData() {
        var deliveryPrice = 2500
        var totalPrice = _itemCartBottomBodyProductTotalPrice + deliveryPrice
        val isAvailableDelivery =
            _itemCartBottomBodyProductTotalPrice >= UiCartBottomBody.MIN_DELIVERY_PRICE
        var isAvailableFreeDelivery = false
        if (_itemCartBottomBodyProductTotalPrice >= UiCartBottomBody.DELIVERY_FREE_PRICE) {
            totalPrice -= deliveryPrice
            deliveryPrice = 0
            if (isAvailableDelivery) isAvailableFreeDelivery = true
        }
        _itemCartBottomBodyData.value = UiCartBottomBody(
            productTotalPrice = _itemCartBottomBodyProductTotalPrice,
            deliveryPrice = deliveryPrice,
            totalPrice = totalPrice,
            isAvailableDelivery = isAvailableDelivery,
            isAvailableFreeDelivery = isAvailableFreeDelivery and isAvailableDelivery,
        )
    }

    private fun getAllRecentlyJoinList() = viewModelScope.launch {
        getJoinUseCase.getRecentlyJoinList().onStart {
            _allRecentlyJoinState.value = UiLocalState.IsLoading(true)
        }.flowOn(dispatcher).catch { exception ->
            _allRecentlyJoinState.value = UiLocalState.IsLoading(false)
            _allRecentlyJoinState.value = UiLocalState.ShowToast(exception.message.toString())
        }.collect {
            _allRecentlyJoinState.value = UiLocalState.IsLoading(false)
            _allRecentlyJoinState.value = UiLocalState.Success(it)
        }
    }

    fun setAppBarTitle(string: String) {
        appBarTitle.value = string
    }

    fun updateUiCartCheckedValue(position: Int, checked: Boolean) {
        _uiCartJoinArrayList[position].checked = checked
        _uiCartJoinList.value = _uiCartJoinArrayList
    }

    fun updateUiCartAmountValue(position: Int, amount: Int) {
        _uiCartJoinArrayList[position].apply {
            this.amount = amount
            totalPrice = sPrice * amount
        }
        _uiCartJoinList.value = _uiCartJoinArrayList
    }

    fun deleteUiCartItemByPos(position: Int, hash: String) {
        _toBeDeletedCartItem.add(hash)
        _uiCartJoinArrayList.removeAt(position)
        _uiCartJoinList.value = _uiCartJoinArrayList
        Log.e(TAG, "deleteUiCartItemByPos: $_toBeDeletedCartItem")
    }

    fun deleteUiCartItemByHash(completion: (complete: Boolean) -> Unit) {
        val success = _uiCartJoinArrayList.removeAll(
            _uiCartJoinArrayList.filter {
                _selectedCartItem.contains(TempOrder(it.hash,it.amount))
            }.toSet()
        )
        _toBeDeletedCartItem.addAll(_selectedCartItem.map { it.hash })
        _uiCartJoinList.value = _uiCartJoinArrayList

        completion(success)
    }

    fun changeCheckedState(checkedValue: Boolean) {
        _uiCartJoinList.value = _uiCartJoinArrayList.onEach {
            it.checked = checkedValue
        }
    }

    fun insertOrderInfo() = CoroutineScope(dispatcher).launch {
        launch {
            val timeStamp = System.currentTimeMillis()
            _selectedCartItem.forEach { tempOrder ->
                insertOrderInfoUseCase(
                    OrderInfo(
                        tempOrder.hash,
                        timeStamp,
                        tempOrder.amount,
                        true
                    )
                )
            }
        }
        launch {
            _selectedCartItem.forEach { tempOrder ->
                Log.e(TAG, "deleteQuery: delete hash ${tempOrder.hash} Thread: ${Thread.currentThread().name}")
                try {
                    deleteCartInfoByHashUseCase(tempOrder.hash)
                } catch (e: CancellationException) {
                    Log.e(TAG, "deleteQuery Delete: $e")
                }
            }
        }
    }

    fun updateAllCartItemChanged() = CoroutineScope(dispatcher).launch {
        cartRepository.insertAndDeleteAllItems(
            _uiCartJoinList.value!!.map {
                CartInfo(
                    it.hash,
                    it.checked,
                    it.amount
                )
            }.toList(),  _toBeDeletedCartItem.toList()
        )
    }

}

