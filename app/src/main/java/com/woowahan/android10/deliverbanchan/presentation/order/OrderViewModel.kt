package com.woowahan.android10.deliverbanchan.presentation.order

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderListItem
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllOrderJoinListUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.InsertOrderInfoUseCase
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartCompleteHeader
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val insertOrderInfoUseCase: InsertOrderInfoUseCase,
    private val getAllOrderJoinListUseCase: GetAllOrderJoinListUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        const val TAG = "OrderViewModel"
    }

    val appBarTitle = MutableLiveData("")
    val orderDetailMode = MutableLiveData(false)
    val currentFragmentIndex = MutableStateFlow<Int>(0)

    var selectedOrderHeader = MutableStateFlow(UiCartCompleteHeader.emptyItem())
    var selectedOrderList = MutableStateFlow<List<UiCartOrderDishJoinItem>>(emptyList())
    var selectedOrderInfo = MutableStateFlow(UiOrderInfo.emptyItem())

    private val _moveToOrderDetailEvent = MutableSharedFlow<Boolean>()
    val moveToOrderDetailEvent = _moveToOrderDetailEvent.asSharedFlow()

    private val _allOrderJoinState =
        MutableStateFlow<UiLocalState<UiOrderListItem>>(UiLocalState.Init)
    val allOrderJoinState: StateFlow<UiLocalState<UiOrderListItem>> get() = _allOrderJoinState

    init {
        getAllOrderList()
    }

    fun selectOrderListItem(orderList: List<UiCartOrderDishJoinItem>) {
        val orderTimeStamp = orderList.first().timeStamp
        val orderItemCount = orderList.map { it.amount }.reduce { acc, uiCartJoinItem -> acc + uiCartJoinItem }
        val deliveryFee = orderList.first().deliveryPrice
        val itemPrice = orderList.map { Pair(it.sPrice, it.amount) }.fold(0) { acc, pair -> acc + pair.first * pair.second }
        selectedOrderHeader.value = UiCartCompleteHeader(orderTimeStamp, orderItemCount)

        selectedOrderList.value = orderList

        selectedOrderInfo.value = UiOrderInfo(itemPrice, deliveryFee, itemPrice + deliveryFee)
    }

    private fun getAllOrderList() {
        viewModelScope.launch {
            getAllOrderJoinListUseCase().onStart {
                _allOrderJoinState.value = UiLocalState.Loading(true)
            }.flowOn(dispatcher).catch { exception ->
                _allOrderJoinState.value = UiLocalState.Loading(false)
                _allOrderJoinState.value = UiLocalState.ShowToast(exception.message.toString())
            }.collect {
                Log.e(TAG, "getAllOrderList: 올더조인플로우 $it", )
                _allOrderJoinState.value = UiLocalState.Loading(false)
                if (it.isEmpty()) _allOrderJoinState.value = UiLocalState.Empty(true)
                else {
                    val map = it.groupBy { it.timeStamp }
                    val list = map.toList().map { (timeStamp, UiCartJointItemLIst) ->
                        UiOrderListItem(
                            timeStamp,
                            UiCartJointItemLIst
                        )
                    }
                    _allOrderJoinState.value = UiLocalState.Success(list)
                }
            }
        }
    }

    fun setAppBarTitle(string: String) {
        appBarTitle.value = string
    }

    fun triggerMoveToOrderDetailFragmentEvent() {
        viewModelScope.launch {
            _moveToOrderDetailEvent.emit(true)
        }
    }
}