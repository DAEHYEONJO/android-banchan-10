package com.woowahan.android10.deliverbanchan.presentation.cart.viewmodel

import androidx.lifecycle.*
import com.woowahan.android10.deliverbanchan.BanChanApplication
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.model.*
import com.woowahan.android10.deliverbanchan.domain.usecase.CartUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllOrderInfoListUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.RecentUseCase
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
    private val recentUseCase: RecentUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        const val TAG = "CartViewModel"
    }

    val fragmentArrayIndex = MutableLiveData(0)
    val appBarTitle = MutableLiveData("")

    val orderDetailMode = MutableLiveData(false)
    private val _orderCompleteTopItem = MutableStateFlow(UiCartCompleteHeader.emptyItem())
    val orderCompleteTopItem: StateFlow<UiCartCompleteHeader> get() = _orderCompleteTopItem

    private val _orderCompleteBodyItem =
        MutableStateFlow<List<UiCartOrderDishJoinItem>>(emptyList())
    val orderCompleteBodyItem: StateFlow<List<UiCartOrderDishJoinItem>> get() = _orderCompleteBodyItem

    private val _orderCompleteFooterItem = MutableStateFlow(UiOrderInfo.emptyItem())
    val orderCompleteFooterItem: StateFlow<UiOrderInfo> get() = _orderCompleteFooterItem

    private val _orderButtonClicked = MutableSharedFlow<Boolean>()
    val orderButtonClicked: SharedFlow<Boolean> = _orderButtonClicked.asSharedFlow()
    val orderBtnClickLiveData = _orderButtonClicked.asLiveData()

    val orderHashList = ArrayList<String>()
    var orderFirstItemTitle = ""

    private val _reloadBtnClicked = MutableLiveData(false)
    val reloadBtnClicked: LiveData<Boolean> get() = _reloadBtnClicked

    private val _allRecentSevenJoinState =
        MutableStateFlow<UiState<List<UiDishItem>>>(UiState.Init)
    val allRecentSevenJoinState: StateFlow<UiState<List<UiDishItem>>>
        get() = _allRecentSevenJoinState.stateIn(
            initialValue = UiState.Init,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    var currentOrderTimeStamp = 0L

    private val _allCartJoinMultiViewTypeState =
        MutableStateFlow<UiState<List<UiCartMultiViewType>>>(UiState.Init)
    val allCartJoinMultiViewTypeState = _allCartJoinMultiViewTypeState.stateIn(
        initialValue = UiState.Init,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )
    private val _toBeDeleteItemsHash = mutableSetOf<String>()

    init {
        getAllRecentSevenJoinList()
        observeOrderInfo()
        getCartJoinMultiViewTypeList()
    }

    private fun getAllRecentSevenJoinList() = viewModelScope.launch {
        cartUseCase.getAllRecentJoinListLimitSeven().onStart {
            _allRecentSevenJoinState.value = UiState.Loading(true)
        }.flowOn(dispatcher).catch { exception ->
            _allRecentSevenJoinState.value = UiState.Loading(false)
            _allRecentSevenJoinState.value = UiState.Error(exception.message.toString())
        }.collect {
            _allRecentSevenJoinState.value = UiState.Loading(false)
            _allRecentSevenJoinState.value = UiState.Success(it)
        }
    }

    private fun getCartJoinMultiViewTypeList() {
        viewModelScope.launch {
            cartUseCase.getCartJoinMultiViewTypeList().onStart {
                _allCartJoinMultiViewTypeState.value = UiState.Loading(true)
            }.flowOn(dispatcher).catch { exception ->
                _allCartJoinMultiViewTypeState.value = UiState.Loading(false)
                _allCartJoinMultiViewTypeState.value = UiState.Error(exception.message.toString())
            }.collect {
                _allCartJoinMultiViewTypeState.value = UiState.Loading(false)
                if (it.isEmpty()) _allCartJoinMultiViewTypeState.value = UiState.Empty(true)
                else _allCartJoinMultiViewTypeState.value = UiState.Success(it)
            }
        }
    }

    fun setAppBarTitle(string: String) {
        appBarTitle.value = string
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

    private fun updatePriceText() {
        _allCartJoinMultiViewTypeState.value.let {
            if (it is UiState.Success) {
                var deliveryPrice = 2500
                var productTotalPrice = 0
                it.items.mapNotNull {
                    it.uiCartOrderDishJoinItem
                }.filter {
                    it.checked
                }.forEach {
                    productTotalPrice += it.amount * it.sPrice
                }
                var totalPrice = productTotalPrice + deliveryPrice
                val isAvailableDelivery = productTotalPrice >= UiCartBottomBody.MIN_DELIVERY_PRICE
                var isAvailableFreeDeliver = false
                if (productTotalPrice >= UiCartBottomBody.DELIVERY_FREE_PRICE) {
                    totalPrice -= deliveryPrice
                    deliveryPrice = 0
                    if (isAvailableDelivery) isAvailableFreeDeliver = true
                }
                val lastPosition = it.items.lastIndex
                _allCartJoinMultiViewTypeState.value = UiState.Success(
                    it.items.toMutableList().apply {
                        set(
                            lastIndex,
                            get(lastIndex).copy(
                                uiCartBottomBody = get(lastPosition).uiCartBottomBody!!.copy(
                                    deliveryPrice = deliveryPrice,
                                    totalPrice = totalPrice,
                                    isAvailableDelivery = isAvailableDelivery,
                                    isAvailableFreeDelivery = isAvailableFreeDeliver,
                                    productTotalPrice = productTotalPrice
                                )
                            )
                        )
                    }
                )
            }
        }
    }

    private fun updateHeaderValue() {
        _allCartJoinMultiViewTypeState.value.let {
            if (it is UiState.Success) {
                val checkedCount = it.items.mapNotNull {
                    it.uiCartOrderDishJoinItem
                }.filter {
                    it.checked
                }.size

                val checkBoxText = if (checkedCount == it.items.size - 2)
                    UiCartHeader.TEXT_SELECT_RELEASE
                else UiCartHeader.TEXT_SELECT_ALL

                val checkBoxChecked = (checkedCount == it.items.size - 2)

                _allCartJoinMultiViewTypeState.value = UiState.Success(
                    it.items.toMutableList().apply {
                        set(
                            0,
                            get(0).copy(
                                uiCartHeader = get(0).uiCartHeader!!.copy(
                                    checkBoxText = checkBoxText,
                                    checkBoxChecked = checkBoxChecked
                                )
                            )
                        )
                    }
                )
            }
        }
    }

    fun updateUiCartCheckedValue(position: Int, checked: Boolean) {
        if (position == -1) return
        _allCartJoinMultiViewTypeState.value.let {
            if (it is UiState.Success) {
                _allCartJoinMultiViewTypeState.value = UiState.Success(
                    it.items.toMutableList().apply {
                        set(
                            position,
                            get(position).copy(
                                uiCartOrderDishJoinItem = get(position).uiCartOrderDishJoinItem!!.copy(
                                    checked = checked
                                )
                            )
                        )
                    }
                )
                updatePriceText()
                updateHeaderValue()
            }
        }
    }

    fun updateUiCartAmountValue(position: Int, amount: Int) {
        if (position == -1) return
        _allCartJoinMultiViewTypeState.value.let {
            if (it is UiState.Success) {
                _allCartJoinMultiViewTypeState.value = UiState.Success(
                    it.items.toMutableList().apply {
                        set(
                            position,
                            get(position).copy(
                                uiCartOrderDishJoinItem = get(position).uiCartOrderDishJoinItem!!.copy(
                                    amount = amount
                                )
                            )
                        )
                    }
                )
                updatePriceText()
            }
        }
    }

    fun deleteUiCartItemByPos(position: Int, hash: String) {
        if (position == -1) return
        _allCartJoinMultiViewTypeState.value.let {
            if (it is UiState.Success) {
                _toBeDeleteItemsHash.add(hash) // 리스트에서는 삭제됐지만 db에 삭제 쿼리 보낼 애들
                // ui에 보이는 리스트 갱신
                _allCartJoinMultiViewTypeState.value = UiState.Success(
                    it.items.toMutableList().apply {
                        removeAt(position)
                    }
                )
                updatePriceText()
                updateHeaderValue()
            }
        }
    }

    fun deleteUiCartItemByHash() {
        _allCartJoinMultiViewTypeState.value.let {
            if (it is UiState.Success) {
                val checkedCartItem = it.items.mapNotNull { uiMultiViewType ->
                    uiMultiViewType.uiCartOrderDishJoinItem
                }.filter { uiCartOrderDishJoinItem ->
                    uiCartOrderDishJoinItem.checked
                }.map {
                    it.hash
                }.toSet()

                _toBeDeleteItemsHash.addAll(checkedCartItem)

                _allCartJoinMultiViewTypeState.value = UiState.Success(
                    it.items.toMutableList().apply {
                        it.items.mapNotNull {
                            it.uiCartOrderDishJoinItem
                        }.apply {
                            removeIf {
                                it.uiCartOrderDishJoinItem != null && checkedCartItem.contains(it.uiCartOrderDishJoinItem!!.hash)
                            }
                        }
                    }
                )
                updatePriceText()
                updateHeaderValue()
            }
        }
    }

    fun changeCheckedState(checkedValue: Boolean) {
        _allCartJoinMultiViewTypeState.value.let {
            if (it is UiState.Success) {
                val header = UiCartMultiViewType(
                    viewType = 0,
                    uiCartHeader = UiCartHeader(
                        checkBoxText = if (checkedValue) UiCartHeader.TEXT_SELECT_RELEASE else UiCartHeader.TEXT_SELECT_ALL,
                        checkBoxChecked = checkedValue
                    )
                )
                val footer = it.items.last()
                val body = it.items.filter { it.viewType == 1 }
                    .toMutableList()
                    .apply {
                        onEachIndexed { index, uiCartMultiViewType ->
                            set(
                                index,
                                uiCartMultiViewType.copy(
                                    uiCartOrderDishJoinItem = get(index).uiCartOrderDishJoinItem!!.copy(
                                        checked = checkedValue
                                    )
                                )
                            )
                        }
                    }
                _allCartJoinMultiViewTypeState.value = UiState.Success(
                    listOf(header) + body + listOf(footer)
                )
                updatePriceText()
            }
        }
    }

    fun setOrderCompleteCartItem() {
        _allCartJoinMultiViewTypeState.value.let {
            if (it is UiState.Success) {
                val tempList = it.items.mapNotNull { it.uiCartOrderDishJoinItem }
                val deliveryInfo = it.items.last().uiCartBottomBody!!
                val orderItems = tempList.filter { it.checked }
                _orderCompleteBodyItem.value = orderItems.sortedBy { it.title }.toList()
                orderFirstItemTitle = _orderCompleteBodyItem.value.first().title
                _orderCompleteTopItem.value = UiCartCompleteHeader(
                    isDelivering = true,
                    orderTimeStamp = System.currentTimeMillis(),
                    orderItemCount = orderItems.size
                )
                _orderCompleteFooterItem.value = UiOrderInfo(
                    itemPrice = deliveryInfo.productTotalPrice,
                    deliveryFee = deliveryInfo.deliveryPrice,
                    totalPrice = deliveryInfo.totalPrice
                )
                orderDetailMode.value = true
                insertOrderInfoDeleteCartInfo()
            }
        }
    }

    private fun insertOrderInfoDeleteCartInfo() {
        BanChanApplication.applicationScope.launch {
            _allCartJoinMultiViewTypeState.value.let {
                if (it is UiState.Success) {
                    // 주문하기 -> orderHashList -> 알람매니저 등록용
                    currentOrderTimeStamp = System.currentTimeMillis()
                    val uiCartOrderDishJoinList = it.items.mapNotNull { it.uiCartOrderDishJoinItem }
                    orderHashList.clear()
                    orderHashList.addAll(
                        uiCartOrderDishJoinList.map { it.hash }
                    )

                    cartUseCase.insertVarArgOrderInfo(
                        tempOrderSet = uiCartOrderDishJoinList.map {
                            TempOrder(
                                it.hash,
                                it.amount,
                                it.title
                            )
                        }.toSet(),
                        timeStamp = currentOrderTimeStamp,
                        isDelivering = true,
                        deliveryPrice = it.items.last().uiCartBottomBody!!.deliveryPrice
                    )
                    _orderButtonClicked.emit(true) //-> 주문햇다고 emit
                    cartUseCase.insertAndDeleteCartItems(
                        uiCartOrderDishJoinList,
                        _toBeDeleteItemsHash.toList()
                    )
                    val deleteFromCartHashList =
                        uiCartOrderDishJoinList.filter { it.checked }.map { it.hash }
                    recentUseCase.updateVarArgRecentIsInsertedFalseInCartUseCase(
                        _toBeDeleteItemsHash.toList()
                    )
                    recentUseCase.updateVarArgRecentIsInsertedFalseInCartUseCase(
                        deleteFromCartHashList
                    )
                    cartUseCase.deleteCartInfoByHashList(
                        deleteFromCartHashList
                    )
                }
            }
        }
    }

    fun updateAllCartItemChanged() {
        BanChanApplication.applicationScope.launch {
            _allCartJoinMultiViewTypeState.value.let {
                if (it is UiState.Success) {
                    cartUseCase.insertAndDeleteCartItems(
                        it.items.mapNotNull { it.uiCartOrderDishJoinItem },
                        _toBeDeleteItemsHash.toList()
                    )
                    recentUseCase.updateVarArgRecentIsInsertedFalseInCartUseCase(
                        _toBeDeleteItemsHash.toList()
                    )
                    _toBeDeleteItemsHash.clear()
                }
            }
        }
    }
}