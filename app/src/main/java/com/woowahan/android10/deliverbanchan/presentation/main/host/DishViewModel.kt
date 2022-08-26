package com.woowahan.android10.deliverbanchan.presentation.main.host

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllCartInfoUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllOrderInfoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    var isReady = false

    init {
        getAllCartInfo()
        getAllOrderInfo()
        isReady = true
    }

    private fun getAllCartInfo() = viewModelScope.launch {
        getAllCartInfoUseCase(this).collect {
            setCartIconText(it.size)
        }
    }

    private fun getAllOrderInfo() = viewModelScope.launch {
        getAllOrderInfoListUseCase(this).collect {
            it.count { order ->
                order.isDelivering
            }.let { currentDeliveringOrderCount ->
                isOrderingExist.value = currentDeliveringOrderCount >= 1
            }
        }
    }

    private fun setCartIconText(listSize: Int) {
        with(listSize) {
            cartIconText.value = if (this in 1..9) {
                toString()
            } else if (this >= 10) {
                "10+"
            } else {
                ""
            }
        }
    }
}