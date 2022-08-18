package com.woowahan.android10.deliverbanchan.presentation.dialogs.bottomsheet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateEmptyUiDishItemUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetCartInfoUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.InsertCartInfoUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.InsertLocalDishUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartBottomSheetViewModel @Inject constructor(
    private val createEmptyUiDishItemUseCase: CreateEmptyUiDishItemUseCase,
    private val getCartInfoUseCase: GetCartInfoUseCase,
    private val insertCartInfoUseCase: InsertCartInfoUseCase,
    private val insertLocalDishUseCase: InsertLocalDishUseCase
) : ViewModel() {

    var currentUiDishItem = MutableStateFlow<UiDishItem>(createEmptyUiDishItemUseCase())

    private val _itemCount = MutableStateFlow<Int>(1)
    val itemCount: StateFlow<Int> = _itemCount

    private val _insertSuccessEvent = MutableSharedFlow<Boolean>()
    val insertSuccessEvent = _insertSuccessEvent.asSharedFlow()

    private var isCurrentItemInserted = false
    private var isCurrentItemChecked = false

    fun getCartInfoByHash() {
        viewModelScope.launch {
            getCartInfoUseCase(currentUiDishItem.value.hash).onStart {

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
        viewModelScope.launch {
            runCatching {
                with(currentUiDishItem.value) {
                    insertCartInfoUseCase(
                        CartInfo(
                            hash = hash,
                            checked = true, // 기본값은 선택된 상태
                            amount = _itemCount.value
                        )
                    )
                    insertLocalDishUseCase(
                        LocalDish(
                            hash = hash,
                            title = title,
                            image = image,
                            nPrice = nPrice,
                            sPrice = sPrice
                        )
                    )
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
        _itemCount.value += 1
    }

    fun clickMinusBtn() {
        if (_itemCount.value >= 2) _itemCount.value -= 1
    }
}