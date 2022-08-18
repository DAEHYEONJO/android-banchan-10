package com.woowahan.android10.deliverbanchan.presentation.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.model.UiCartJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiRecentlyJoinItem
import com.woowahan.android10.deliverbanchan.domain.usecase.DeleteCartInfoByHashUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetJoinUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.UpdateCartAmount
import com.woowahan.android10.deliverbanchan.domain.usecase.UpdateCartChecked
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartBottomBody
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getJoinUseCase: GetJoinUseCase,
    private val deleteCartInfoByHashUseCase: DeleteCartInfoByHashUseCase,
    private val updateCartChecked: UpdateCartChecked,
    private val updateCartAmount: UpdateCartAmount,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        const val TAG = "CartViewModel"
    }

    val appBarTitle = MutableLiveData("")
    val orderDetailMode = MutableLiveData(false)

    private val _allCartJoinState = MutableStateFlow<UiLocalState<UiCartJoinItem>>(UiLocalState.Init)
    val allCartJoinState: StateFlow<UiLocalState<UiCartJoinItem>> get() = _allCartJoinState.stateIn(
        initialValue = UiLocalState.Init,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )
    private val _allRecentlyJoinState = MutableStateFlow<UiLocalState<UiRecentlyJoinItem>>(UiLocalState.Init)
    val allRecentlyJoinState: StateFlow<UiLocalState<UiRecentlyJoinItem>> get() = _allRecentlyJoinState.stateIn(
        initialValue = UiLocalState.Init,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    val itemCartHeaderChecked = MutableLiveData(false)
    private var _itemCartBottomBodyProductTotalPrice = 0
    private val _itemCartBottomBodyData = MutableLiveData(UiCartBottomBody.emptyItem())
    val itemCartBottomBodyData: LiveData<UiCartBottomBody> get() = _itemCartBottomBodyData

    init {
        getAllRecentlyJoinList()
        getAllCartJoinList()
    }
    private fun getAllCartJoinList() = viewModelScope.launch {
        getJoinUseCase.getCartJoinList().onStart {
            _allCartJoinState.value = UiLocalState.IsLoading(true)
        }.flowOn(dispatcher).catch { exception ->
            _allCartJoinState.value = UiLocalState.IsLoading(false)
            _allCartJoinState.value = UiLocalState.ShowToast(exception.message.toString())
        }.onEach { uiCartJoinItem ->
            _itemCartBottomBodyProductTotalPrice = 0
            val checkedUiJoinCartItem = uiCartJoinItem.filter { it.checked }
            checkedUiJoinCartItem.forEach { checkedUiCartJoinItem ->
                _itemCartBottomBodyProductTotalPrice+=checkedUiCartJoinItem.totalPrice
            }
            itemCartHeaderChecked.value = checkedUiJoinCartItem.isNotEmpty()
        }.collect{
            _allCartJoinState.value = UiLocalState.IsLoading(false)
            _allCartJoinState.value = UiLocalState.Success(it)
            setItemCartBottomBodyData(it)
        }
    }

    private fun setItemCartBottomBodyData(uiCartJoinItems: List<UiCartJoinItem>){
        var deliveryPrice = 2500
        var totalPrice = _itemCartBottomBodyProductTotalPrice + deliveryPrice
        val isAvailableDelivery = totalPrice >= UiCartBottomBody.MIN_DELIVERY_PRICE
        var isAvailableFreeDelivery = false
        if (_itemCartBottomBodyProductTotalPrice >= UiCartBottomBody.DELIVERY_FREE_PRICE){
            totalPrice -= deliveryPrice
            deliveryPrice = 0
            isAvailableFreeDelivery = true
        }
        _itemCartBottomBodyData.value = UiCartBottomBody(
            productTotalPrice = _itemCartBottomBodyProductTotalPrice,
            deliveryPrice = deliveryPrice,
            totalPrice = totalPrice,
            isAvailableDelivery = isAvailableDelivery,
            isAvailableFreeDelivery = isAvailableFreeDelivery,
        )
    }

    private fun getAllRecentlyJoinList() = viewModelScope.launch {
        getJoinUseCase.getRecentlyJoinList().onStart {
            _allRecentlyJoinState.value = UiLocalState.IsLoading(true)
        }.flowOn(dispatcher).catch { exception ->
            _allRecentlyJoinState.value = UiLocalState.IsLoading(false)
            _allRecentlyJoinState.value = UiLocalState.ShowToast(exception.message.toString())
        }.collect{
            _allRecentlyJoinState.value = UiLocalState.IsLoading(false)
            _allRecentlyJoinState.value = UiLocalState.Success(it)
        }
    }

    fun deleteCart(hash: String) = viewModelScope.launch {
        deleteCartInfoByHashUseCase(hash)
    }

    fun updateCartCheckedValue(hash: String, checked: Boolean) = viewModelScope.launch {
        Log.e(TAG, "updateCartCheckedValue: ", )
        updateCartChecked(hash, checked)
    }

    fun updateCartAmountValue(hash: String, amount: Int) = viewModelScope.launch {
        updateCartAmount(hash, amount)
    }

    fun setAppBarTitle(string: String) {
        appBarTitle.value = string
    }

}

