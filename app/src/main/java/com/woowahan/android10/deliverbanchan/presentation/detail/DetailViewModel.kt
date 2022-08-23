package com.woowahan.android10.deliverbanchan.presentation.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.*
import com.woowahan.android10.deliverbanchan.presentation.state.UiDetailState
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailDishUseCase: GetDetailDishUseCase,
    private val insertRecentUseCase: InsertRecentUseCase,
    private val insertCartInfoUseCase: InsertCartInfoUseCase,
    private val updateCartAmount: UpdateCartAmount,
    private val getAllCartInfoUseCase: GetAllCartInfoUseCase,
    private val getAllOrderInfoListUseCase: GetAllOrderInfoListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Cart Flow 감지하고(어차피 app bar 용도로 만들어 주어야함.) 현재 hash에 해당하는 isInserted값 업데이트하기


    val cartIconText = MutableLiveData("")
    val isOrderingExist = MutableLiveData(false)

    private val currentUiDishItem: UiDishItem? = savedStateHandle["UiDishItem"]
    private val _uiDetailInfo = MutableStateFlow<UiDetailState>(UiDetailState.Init)
    val uiDetailInfo: StateFlow<UiDetailState> = _uiDetailInfo

    private val _itemCount = MutableStateFlow<Int>(1)
    val itemCount: StateFlow<Int> = _itemCount

    private val _insertSuccessEvent = MutableSharedFlow<Boolean>()
    val insertSuccessEvent = _insertSuccessEvent.asSharedFlow()

    init {
        getAllCartInfo()
        getAllOrderInfo()
        getDetailDishInfo()
    }

    private fun getAllCartInfo() = viewModelScope.launch {
        getAllCartInfoUseCase(this).collect { cartInfoList ->
            setCartIconText(cartInfoList.size)
            currentUiDishItem?.let {
                it.isInserted = cartInfoList
                    .map { cartInfo ->
                        cartInfo.hash
                    }.toSet()
                    .contains(it.hash)
            }
        }
    }

    private fun getAllOrderInfo() = viewModelScope.launch {

        getAllOrderInfoListUseCase(this).collect {
            val deliveringOrder: OrderInfo? = it.find { order ->
                order.isDelivering
            }
            isOrderingExist.value = deliveringOrder?.let { true } ?: false
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

    private fun insertRecent() {
        currentUiDishItem?.let { dishItem ->
            viewModelScope.launch {
                with(dishItem) {
                    insertRecentUseCase(
                        LocalDish(
                            hash, title, image, description, nPrice, sPrice
                        ),
                        RecentViewedInfo(
                            hash = hash, timeStamp = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }

    private fun getDetailDishInfo() {
        viewModelScope.launch {
            getDetailDishUseCase(
                currentUiDishItem!!.hash,
                currentUiDishItem,
                _itemCount.value
            ).onStart {
                _uiDetailInfo.value = UiDetailState.Loading(true)
            }.catch { exception ->
                _uiDetailInfo.value = UiDetailState.Loading(false)
                _uiDetailInfo.value = UiDetailState.ShowToast(exception.message.toString())
            }.flowOn(Dispatchers.IO).collect { result ->
                _uiDetailInfo.value = UiDetailState.Loading(false)
                when (result) {
                    is BaseResult.Success -> {
                        _uiDetailInfo.value = UiDetailState.Success(result.data)
                        insertRecent()
                    }
                    is BaseResult.Error -> {
                        _uiDetailInfo.value = UiDetailState.Error(result.errorCode)
                    }
                }
            }
        }
    }

    fun orderDetailItem() {
        viewModelScope.launch {
            currentUiDishItem?.let {
                runCatching {
                    if (it.isInserted) {
                        // 이미 장바구니에 있는 경우
                        updateCartAmount(hash = it.hash, amount = _itemCount.value)
                    } else {
                        //새로 장바구니에 들어가는 경우
                        insertCartInfoUseCase(
                            CartInfo(
                                hash = it.hash,
                                checked = true,
                                amount = _itemCount.value
                            )
                        )
                    }
                }.onSuccess {
                    _insertSuccessEvent.emit(true)
                }.onFailure {
                    _insertSuccessEvent.emit(false)
                }
            }
        }
    }

    fun plusItemCount() {
        _itemCount.value += 1
        _uiDetailInfo.value = UiDetailState.Success(
            (_uiDetailInfo.value as UiDetailState.Success).uiDishItems.copy(itemCount = _itemCount.value)
        )
    }

    fun minusItemCount() {
        if (_itemCount.value >= 2) {
            _itemCount.value -= 1
            _uiDetailInfo.value = UiDetailState.Success(
                (_uiDetailInfo.value as UiDetailState.Success).uiDishItems.copy(itemCount = _itemCount.value)
            )
        }
    }
}