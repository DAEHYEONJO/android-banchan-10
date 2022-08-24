package com.woowahan.android10.deliverbanchan.presentation.cart

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.google.gson.Gson
import com.woowahan.android10.deliverbanchan.background.CartItemsDbWorker
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderInfo
import com.woowahan.android10.deliverbanchan.domain.usecase.*
import com.woowahan.android10.deliverbanchan.presentation.cart.model.TempOrder
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartBottomBody
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartCompleteHeader
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartHeader
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getJoinUseCase: GetJoinUseCase,
    private val insertAndDeleteCartItemsUseCase: InsertAndDeleteCartItemsUseCase,
    private val deleteCartInfoByHashListUseCase: DeleteCartInfoByHashListUseCase,
    private val insertVarArgOrderInfoUseCase: InsertVarArgOrderInfoUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    application: Application
) : ViewModel() {

    companion object {
        const val TAG = "CartViewModel"
    }

    val fragmentArrayIndex = MutableLiveData(0)
    private val workManager = WorkManager.getInstance(application)

    val appBarTitle = MutableLiveData("")
    val orderDetailMode = MutableLiveData(false)

    private val _allCartJoinState =
        MutableStateFlow<UiLocalState<UiCartOrderDishJoinItem>>(UiLocalState.Init)
    val allCartJoinState: StateFlow<UiLocalState<UiCartOrderDishJoinItem>>
        get() = _allCartJoinState.stateIn(
            initialValue = UiLocalState.Init,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )
    private val _allRecentlyJoinState =
        MutableStateFlow<UiLocalState<UiDishItem>>(UiLocalState.Init)
    val allRecentlyJoinState: StateFlow<UiLocalState<UiDishItem>>
        get() = _allRecentlyJoinState.stateIn(
            initialValue = UiLocalState.Init,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private var _itemCartBottomBodyProductTotalPrice = 0

    private val _itemCartHeaderData = MutableLiveData(UiCartHeader.emptyItem())
    val itemCartHeaderData: LiveData<UiCartHeader> get() = _itemCartHeaderData

    private val _uiCartJoinList = MutableLiveData<List<UiCartOrderDishJoinItem>>(emptyList())
    val uiCartJoinList: LiveData<List<UiCartOrderDishJoinItem>> get() = _uiCartJoinList
    private val _uiCartJoinArrayList = ArrayList<UiCartOrderDishJoinItem>()

    private val _itemCartBottomBodyData = MutableLiveData(UiCartBottomBody.emptyItem())
    val itemCartBottomBodyData: LiveData<UiCartBottomBody> get() = _itemCartBottomBodyData

    private val _selectedCartItem = mutableSetOf<TempOrder>()

    private val _toBeDeletedCartItem = mutableSetOf<String>()

    private val _orderCompleteTopItem = MutableLiveData<UiCartCompleteHeader>()
    val orderCompleteTopItem: LiveData<UiCartCompleteHeader> get() = _orderCompleteTopItem

    private val _orderCompleteBodyItem = MutableLiveData<List<UiCartOrderDishJoinItem>>(emptyList())
    val orderCompleteBodyItem: LiveData<List<UiCartOrderDishJoinItem>> get() = _orderCompleteBodyItem

    private val _orderCompleteFooterItem = MutableLiveData<UiOrderInfo>()
    val orderCompleteFooterItem: LiveData<UiOrderInfo> get() = _orderCompleteFooterItem

    private val _orderButtonClicked = MutableSharedFlow<Boolean>()
    val orderButtonClicked: SharedFlow<Boolean> = _orderButtonClicked.asSharedFlow()

    val orderHashList = ArrayList<String>()
    var orderFirstItemTitle = "Title"

    init {
        getAllRecentlyJoinList()
        getAllCartJoinList()
    }


    internal fun updateCartDataBase() {
        val request = OneTimeWorkRequestBuilder<CartItemsDbWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(getCartWorkerData())
            .build()

        workManager.enqueue(request)
    }

    fun getCartWorkerData(): Data {
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
        val builder = Data.Builder().apply {
            putString("cartListStr", cartListStr)
            putStringArray("deleteListStr", deleteList.toTypedArray())
        }
        return builder.build()
    }

    private fun getAllCartJoinList() = viewModelScope.launch {
        getJoinUseCase.getCartJoinList().onStart {
            _allCartJoinState.value = UiLocalState.Loading(true)
        }.flowOn(dispatcher).catch { exception ->
            _allCartJoinState.value = UiLocalState.Loading(false)
            _allCartJoinState.value = UiLocalState.ShowToast(exception.message.toString())
        }.onEach { uiCartJoinItemList ->
            calcCartBottomBodyAndHeaderVal(uiCartJoinItemList)
        }.collect {
            _allCartJoinState.value = UiLocalState.Loading(false)
            _allCartJoinState.value = UiLocalState.Success(it)
            _uiCartJoinArrayList.clear()
            _uiCartJoinArrayList.addAll(it)
            _uiCartJoinList.value = _uiCartJoinArrayList
        }
    }

    fun calcCartBottomBodyAndHeaderVal(uiCartOrderDishJoinItemList: List<UiCartOrderDishJoinItem>) {
        _itemCartBottomBodyProductTotalPrice = 0
        _selectedCartItem.clear()
        val checkedUiJoinCartItem = uiCartOrderDishJoinItemList.filter { it.checked }
        checkedUiJoinCartItem.forEach { checkedUiCartJoinItem ->
            _selectedCartItem.add(
                TempOrder(
                    checkedUiCartJoinItem.hash,
                    checkedUiCartJoinItem.amount,
                    checkedUiCartJoinItem.title
                )
            )
            _itemCartBottomBodyProductTotalPrice += checkedUiCartJoinItem.totalPrice
        }
        if (checkedUiJoinCartItem.size == uiCartOrderDishJoinItemList.size) {
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
        getJoinUseCase.getAllRecentJoinListLimitSeven().onStart {
            _allRecentlyJoinState.value = UiLocalState.Loading(true)
        }.flowOn(dispatcher).catch { exception ->
            _allRecentlyJoinState.value = UiLocalState.Loading(false)
            _allRecentlyJoinState.value = UiLocalState.ShowToast(exception.message.toString())
        }.collect {
            _allRecentlyJoinState.value = UiLocalState.Loading(false)
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
        if (position == -1) return // 클릭했을때, 없어진 view의 경우 position == -1
        _toBeDeletedCartItem.add(hash)
        _uiCartJoinArrayList.removeAt(position)
        _uiCartJoinList.value = _uiCartJoinArrayList
    }

    fun deleteUiCartItemByHash(completion: (complete: Boolean) -> Unit) {
        val success = _uiCartJoinArrayList.removeAll(
            _uiCartJoinArrayList.filter {
                _selectedCartItem.contains(TempOrder(it.hash, it.amount, it.title))
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

    fun setOrderCompleteCartItem() {
        // 주문 완료 화면에 대한 리스트 세팅z`z`
        val tempHashList = _selectedCartItem.map { it.hash }.toList()
        _orderCompleteBodyItem.value =
            _uiCartJoinArrayList.filter { tempHashList.contains(it.hash) }.toList()
        val deliveryPrice = _itemCartBottomBodyData.value!!.deliveryPrice
        val priceTotal = _orderCompleteBodyItem.value!!
            .map { Pair(it.sPrice, it.amount) }
            .fold(0) { acc, pair ->
                acc + pair.first * pair.second
            }
        val totalPrice = priceTotal + deliveryPrice
        _orderCompleteTopItem.value = UiCartCompleteHeader(
            orderTimeStamp = System.currentTimeMillis(),
            orderItemCount = _orderCompleteBodyItem.value!!.size
        )
        _orderCompleteFooterItem.value = UiOrderInfo(
            itemPrice = priceTotal,
            deliveryFee = deliveryPrice,
            totalPrice = totalPrice
        )
        orderDetailMode.value = true
        insertOrderInfoDeleteCartInfo()
    }

    private fun insertOrderInfoDeleteCartInfo() {
        CoroutineScope(dispatcher).launch {
            val timeStamp = System.currentTimeMillis()
            orderHashList.clear()
            orderFirstItemTitle = "Title"
            insertVarArgOrderInfoUseCase(
                _selectedCartItem.mapIndexed { inx, tempOrder ->
                    if (inx == 0) orderFirstItemTitle = tempOrder.title
                    orderHashList.add(tempOrder.hash)
                    OrderInfo(
                        hash = tempOrder.hash,
                        timeStamp = timeStamp,
                        amount = tempOrder.amount,
                        isDelivering = true,
                        deliveryPrice = _itemCartBottomBodyData.value!!.deliveryPrice
                    )
                }
            )
            _orderButtonClicked.emit(true)
            deleteCartInfoByHashListUseCase(_selectedCartItem.map { it.hash }.toList())
        }
    }

    fun updateAllCartItemChanged() {
        CoroutineScope(dispatcher).launch {
            insertAndDeleteCartItemsUseCase(
                _uiCartJoinList.value!!.map {
                    CartInfo(
                        it.hash,
                        it.checked,
                        it.amount
                    )
                }.toList(), _toBeDeletedCartItem.toList()
            )
        }
    }
}


