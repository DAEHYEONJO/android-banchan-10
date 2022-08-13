package com.woowahan.android10.deliverbanchan.presentation.dialogs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateEmptyUiDishItemUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetCartInfoUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.InsertCartInfoUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.IsExistCartInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartBottomSheetViewModel @Inject constructor(
    private val createEmptyUiDishItemUseCase: CreateEmptyUiDishItemUseCase,
    private val isExistCartInfoUseCase: IsExistCartInfoUseCase,
    private val getCartInfoUseCase: GetCartInfoUseCase,
    private val insertCartInfoUseCase: InsertCartInfoUseCase
) : ViewModel() {

    var currentUiDishItem = MutableStateFlow<UiDishItem>(createEmptyUiDishItemUseCase())

    private val _itemCount = MutableStateFlow<Int>(1)
    val itemCount: StateFlow<Int> = _itemCount

    private var isCurrentItemInserted = false

    fun getCartInfoByHash() {
        viewModelScope.launch {
            getCartInfoUseCase(currentUiDishItem.value.hash).onStart {

            }.catch { exception ->
                Log.e("CartBottomSheetViewModel", "${exception.message}")
            }.flowOn(Dispatchers.IO).collect { cartInfo ->
                Log.e("CartBottomSheetViewModel", "cartInfo : ${cartInfo}")

                if(cartInfo == null) {
                    Log.e("CartBottomSheetViewModel", "null")
                    isCurrentItemInserted = false
                } else {
                    isCurrentItemInserted = true
                    _itemCount.value = cartInfo.amount
                }
            }
        }
    }

    fun insertCartInfo() {
        viewModelScope.launch {

        }
    }

    fun clickPlusBtn() {
        _itemCount.value += 1
    }

    fun clickMinusBtn() {
        if(_itemCount.value >= 2) _itemCount.value -= 1
    }
}