package com.woowahan.android10.deliverbanchan.presentation.dialogs.bottomsheet

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.BanChanApplication
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartBottomSheetViewModel @Inject constructor(
    private val cartUseCase: CartUseCase,
    private val insertCartInfoUseCase: InsertCartInfoUseCase,
    private val insertLocalDishAndRecentUseCase: InsertLocalDishAndRecentUseCase,
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

    init {
        getCartInfoByHash()
    }

    private fun getCartInfoByHash() {
        viewModelScope.launch {
            cartUseCase.getBottomSheetInfoByHash(uiDishItem!!.hash)
                .flowOn(Dispatchers.IO).collect { uiBottomSheet ->
                    if (uiBottomSheet.amount == -1) {
                        isCurrentItemInserted = false
                        isCurrentItemChecked = false
                    } else {
                        isCurrentItemInserted = true
                        isCurrentItemChecked = uiBottomSheet.checked
                        _itemCount.value = uiBottomSheet.amount
                    }
                }
        }
    }

    fun insertCartInfo() {
        viewModelScope.launch {
            runCatching {
                with(uiDishItem!!) {
                    insertLocalDishAndRecentUseCase(
                        uiDishItem,
                        hash,
                        System.currentTimeMillis(),
                        true
                    )
                    insertCartInfoUseCase(hash = hash, checked = true, amount = _itemCount.value)
                }
            }.onSuccess {
                _insertSuccessEvent.emit(true)
            }.onFailure {
                _insertSuccessEvent.emit(false)
            }
        }
    }

    fun clickPlusBtn() {
        if (_itemCount.value == 20) return
        _itemCount.value += 1
    }

    fun clickMinusBtn() {
        if (_itemCount.value >= 2) _itemCount.value -= 1
    }
}