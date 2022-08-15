package com.woowahan.android10.deliverbanchan.presentation.main.host

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.local.model.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.Order
import com.woowahan.android10.deliverbanchan.data.local.model.OrderDish
import com.woowahan.android10.deliverbanchan.data.local.model.OrderInfo
import com.woowahan.android10.deliverbanchan.domain.usecase.*
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishViewModel @Inject constructor(
    private val getAllCartInfoUseCase: GetAllCartInfoUseCase,
    private val getAllOrderInfoListUseCase: GetAllOrderInfoListUseCase
) : ViewModel() {

    companion object {
        const val TAG = "DishViewModel"
    }
    private val _cartInfoState = MutableStateFlow<UiLocalState<CartInfo>>(UiLocalState.Init)
    val cartInfoState: StateFlow<UiLocalState<CartInfo>> get() = _cartInfoState
    val cartIconText = MutableLiveData("")

    private val _orderState = MutableStateFlow<UiLocalState<OrderInfo>>(UiLocalState.Init)
    val orderState: StateFlow<UiLocalState<OrderInfo>> get() = _orderState
    val isOrderingExist = MutableLiveData(false)

    init {
        getAllCartInfo()
        getAllOrderInfo()
    }

    private fun getAllCartInfo() = viewModelScope.launch {
        getAllCartInfoUseCase().catch { exception ->
            _cartInfoState.value = UiLocalState.ShowToast(exception.message.toString())
        }.collect{
            _cartInfoState.value = UiLocalState.Success(it)
            setCartIconText((cartInfoState.value as UiLocalState.Success).uiDishItems.size)
        }
    }

    private fun getAllOrderInfo() = viewModelScope.launch {
        getAllOrderInfoListUseCase().catch { exception ->
            _orderState.value = UiLocalState.ShowToast(exception.message.toString())
        }.collect{
            _orderState.value = UiLocalState.Success(it)
            val deliveringOrder: OrderInfo? = it.find { order ->
                order.isDelivering
            }
            isOrderingExist.value = deliveringOrder?.let { true } ?: false
        }
    }

    private fun setCartIconText(listSize: Int){
        with(listSize){
            cartIconText.value = if ( this in 1 .. 9 ){
                toString()
            }else if ( this >= 10){
                "${toString()}+"
            }else{
                ""
            }
        }
    }
}