package com.woowahan.android10.deliverbanchan.presentation.cart

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.woowahan.android10.deliverbanchan.BanChanApplication
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.model.TempOrder
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderInfo
import com.woowahan.android10.deliverbanchan.domain.usecase.CartUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllOrderInfoListUseCase
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartBottomBody
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartCompleteHeader
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartHeader
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getAllOrderInfoListUseCase: GetAllOrderInfoListUseCase,
    private val cartUseCase: CartUseCase,
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
        MutableStateFlow<UiState<List<UiCartOrderDishJoinItem>>>(UiState.Init)
    val allCartJoinState: StateFlow<UiState<List<UiCartOrderDishJoinItem>>>
        get() = _allCartJoinState.stateIn(
            initialValue = UiState.Init,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )
    private val _allRecentlyJoinState =
        MutableStateFlow<UiState<List<UiDishItem>>>(UiState.Init)
    val allRecentlyJoinState: StateFlow<UiState<List<UiDishItem>>>
        get() = _allRecentlyJoinState.stateIn(
            initialValue = UiState.Init,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private var _itemCartBottomBodyProductTotalPrice = 0
    var currentOrderTimeStamp = 0L

    private val _itemCartHeaderData = MutableLiveData(UiCartHeader.emptyItem())
    val itemCartHeaderData: LiveData<UiCartHeader> get() = _itemCartHeaderData

    private val _uiCartJoinList = MutableLiveData<List<UiCartOrderDishJoinItem>>(emptyList())
    val uiCartJoinList: LiveData<List<UiCartOrderDishJoinItem>> get() = _uiCartJoinList
    private val _uiCartJoinArrayList = ArrayList<UiCartOrderDishJoinItem>()

    private val _itemCartBottomBodyData = MutableLiveData(UiCartBottomBody.emptyItem())
    val itemCartBottomBodyData: LiveData<UiCartBottomBody> get() = _itemCartBottomBodyData

    private val _selectedCartItem = mutableSetOf<TempOrder>()

    private val _toBeDeletedCartItem = mutableSetOf<String>()

    private val _orderCompleteTopItem = MutableStateFlow(UiCartCompleteHeader.emptyItem())
    val orderCompleteTopItem: StateFlow<UiCartCompleteHeader> get() = _orderCompleteTopItem

    private val _orderCompleteBodyItem =
        MutableStateFlow<List<UiCartOrderDishJoinItem>>(emptyList())
    val orderCompleteBodyItem: StateFlow<List<UiCartOrderDishJoinItem>> get() = _orderCompleteBodyItem

    private val _orderCompleteFooterItem = MutableStateFlow(UiOrderInfo.emptyItem())
    val orderCompleteFooterItem: StateFlow<UiOrderInfo> get() = _orderCompleteFooterItem

    private val _orderButtonClicked = MutableSharedFlow<Boolean>()
    val orderButtonClicked: SharedFlow<Boolean> = _orderButtonClicked.asSharedFlow()

    val orderHashList = ArrayList<String>()
    var orderFirstItemTitle = "Title"

    private val _reloadBtnClicked = MutableLiveData(false)
    val reloadBtnClicked: LiveData<Boolean> get() = _reloadBtnClicked

    init {
        getAllRecentlyJoinList()
        getAllCartJoinList()
        observeOrderInfo()
    }

    fun setReloadBtnValue() {
        _reloadBtnClicked.value = true
    }

    private fun observeOrderInfo() {
        viewModelScope.launch {
            getAllOrderInfoListUseCase(this).collect {
                val orderListTimeStampMap = it.groupBy { it.timeStamp }
                if (_orderCompleteBodyItem.value.isNotEmpty()) {
                    if (orderListTimeStampMap.keys.contains(currentOrderTimeStamp)) {
                        _orderCompleteTopItem.value =
                            _orderCompleteTopItem.value.copy(
                                isDelivering = orderListTimeStampMap[currentOrderTimeStamp]!!.first().isDelivering
                            )
                    }
                }
            }
        }
    }

    private fun getAllCartJoinList() = viewModelScope.launch {
        cartUseCase.getCartJoinList().onStart {
            _allCartJoinState.value = UiState.Loading(true)
        }.flowOn(dispatcher).catch { exception ->
            _allCartJoinState.value = UiState.Loading(false)
            _allCartJoinState.value = UiState.Error(exception.message.toString())
        }.onEach { uiCartJoinItemList ->
            calcCartBottomBodyAndHeaderVal(uiCartJoinItemList)
        }.collect {
            _allCartJoinState.value = UiState.Loading(false)
            _allCartJoinState.value = UiState.Success(it)
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
        cartUseCase.getAllRecentJoinListLimitSeven().onStart {
            _allRecentlyJoinState.value = UiState.Loading(true)
        }.flowOn(dispatcher).catch { exception ->
            _allRecentlyJoinState.value = UiState.Loading(false)
            _allRecentlyJoinState.value = UiState.Error(exception.message.toString())
        }.collect {
            _allRecentlyJoinState.value = UiState.Loading(false)
            _allRecentlyJoinState.value = UiState.Success(it)
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
        // 주문 완료 화면에 대한 리스트 세팅
        val tempHashList = _selectedCartItem.map { it.hash }.toList()
        _orderCompleteBodyItem.value =
            _uiCartJoinArrayList.filter { tempHashList.contains(it.hash) }.toList()
        val deliveryPrice = _itemCartBottomBodyData.value!!.deliveryPrice
        val priceTotal = _orderCompleteBodyItem.value
            .map { Pair(it.sPrice, it.amount) }
            .fold(0) { acc, pair ->
                acc + pair.first * pair.second
            }
        val totalPrice = priceTotal + deliveryPrice
        val orderItemCount = _orderCompleteBodyItem.value.map { it.amount }
            .reduce { sum, eachAmount -> sum + eachAmount }
        _orderCompleteTopItem.value = UiCartCompleteHeader(
            isDelivering = true,
            orderTimeStamp = System.currentTimeMillis(),
            orderItemCount = orderItemCount
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
        BanChanApplication.applicationScope.launch {
            currentOrderTimeStamp = System.currentTimeMillis()
            orderHashList.clear()
            orderFirstItemTitle = "Title"
            _selectedCartItem.forEachIndexed { index, tempOrder ->
                if (index == 0) orderFirstItemTitle = tempOrder.title
                orderHashList.add(tempOrder.hash)
            }
            cartUseCase.insertVarArgOrderInfo(
                tempOrderSet = _selectedCartItem,
                timeStamp = currentOrderTimeStamp,
                isDelivering = true,
                deliveryPrice = _itemCartBottomBodyData.value!!.deliveryPrice
            )
            _orderButtonClicked.emit(true)
            cartUseCase.deleteCartInfoByHashList(_selectedCartItem.map { it.hash }.toList())
        }
    }

    fun updateAllCartItemChanged() {
        BanChanApplication.applicationScope.launch {
            cartUseCase.insertAndDeleteCartItems(
                _uiCartJoinList.value!!, _toBeDeletedCartItem.toList()
            )
        }
    }
}