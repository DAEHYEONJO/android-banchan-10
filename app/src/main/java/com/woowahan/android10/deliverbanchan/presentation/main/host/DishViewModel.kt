package com.woowahan.android10.deliverbanchan.presentation.main.host

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
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
    val cartIconText = MutableLiveData("")
    val isOrderingExist = MutableLiveData(false)

    init {
        getAllCartInfo()
        getAllOrderInfo()
    }

    private fun getAllCartInfo() = viewModelScope.launch {
        getAllCartInfoUseCase(this).collect{
            setCartIconText(it.size)
        }
    }

    private fun getAllOrderInfo() = viewModelScope.launch {
        getAllOrderInfoListUseCase(this).collect{
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
                "10+"
            }else{
                ""
            }
        }
    }
}