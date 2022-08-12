package com.woowahan.android10.deliverbanchan.presentation.state

import com.woowahan.android10.deliverbanchan.data.local.model.CartInfo

sealed class UiCartState{
    object Init : UiCartState()
    data class ShowToast(val message: String) : UiCartState()
    data class Success(val uiDishItems: List<CartInfo>) : UiCartState()
}
