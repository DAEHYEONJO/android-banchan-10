package com.woowahan.android10.deliverbanchan.presentation.dialogs.bottomsheet

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.woowahan.android10.deliverbanchan.BanChanApplication
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CartUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.InsertCartInfoUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.InsertLocalDishUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.UpdateTimeStampRecentViewedByHashUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartBottomSheetViewModel @Inject constructor(
    private val cartUseCase: CartUseCase,
    private val insertCartInfoUseCase: InsertCartInfoUseCase,
    private val insertLocalDishUseCase: InsertLocalDishUseCase,
    private val updateTimeStampRecentViewedByHashUseCase: UpdateTimeStampRecentViewedByHashUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {
    var currentUiDishItem = MutableStateFlow<UiDishItem>(UiDishItem.returnEmptyItem())

    val uiDishItem: UiDishItem? = stateHandle["UiDishItem"]

    private val _itemCount = MutableStateFlow<Int>(1)
    val itemCount: StateFlow<Int> = _itemCount

    private val _insertSuccessEvent = MutableSharedFlow<Boolean>()
    val insertSuccessEvent = _insertSuccessEvent.asSharedFlow()

    private var isCurrentItemInserted = false
    private var isCurrentItemChecked = false

    fun getCartInfoByHash() {
        Log.e("CartBottomSheetViewModel", "유아이디시아이템: uiDishItem: ${uiDishItem}", )
        BanChanApplication.applicationScope.launch {
            cartUseCase.getCartInfoByHash(uiDishItem!!.hash).onStart {

            }.catch { exception ->
                Log.e("CartBottomSheetViewModel", "${exception.message}")
            }.flowOn(Dispatchers.IO).collect { cartInfo ->
                Log.e("CartBottomSheetViewModel", "cartInfo : ${cartInfo}")

                if (cartInfo == null) {
                    Log.e("CartBottomSheetViewModel", "null")
                    isCurrentItemInserted = false
                    isCurrentItemChecked = false
                } else {
                    isCurrentItemInserted = true
                    isCurrentItemChecked = cartInfo.checked
                    _itemCount.value = cartInfo.amount
                }
            }
        }
    }

    fun insertCartInfo() {
        BanChanApplication.applicationScope.launch {
            runCatching {
                with(uiDishItem!!) {
                    updateTimeStampRecentViewedByHashUseCase(hash, System.currentTimeMillis())
                    insertCartInfoUseCase(hash = hash, checked = true, amount = _itemCount.value)
                    insertLocalDishUseCase(uiDishItem)
                }
            }.onSuccess {
                Log.e("CartBottomSheetViewModel", "insert success")
                _insertSuccessEvent.emit(true)
            }.onFailure {
                Log.e("CartBottomSheetViewModel", "insert fail")
                _insertSuccessEvent.emit(false)
            }
        }
    }

    fun clickPlusBtn() {
        if (_itemCount.value==20) return
        _itemCount.value += 1
    }

    fun clickMinusBtn() {
        if (_itemCount.value >= 2) _itemCount.value -= 1
    }
}