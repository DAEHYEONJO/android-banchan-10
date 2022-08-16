package com.woowahan.android10.deliverbanchan.presentation.main.host

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Cart
import com.woowahan.android10.deliverbanchan.data.local.model.join.Order
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentlyViewed
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.DishRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentlyViewedRepository
import com.woowahan.android10.deliverbanchan.domain.usecase.*
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
        }.flowOn(Dispatchers.IO).collect{
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