package com.woowahan.android10.deliverbanchan.presentation.state

sealed class UiDbState<out T> {
    object Init : UiDbState<Nothing>()
    data class IsLoading(val isLoading: Boolean) : UiDbState<Nothing>()
    data class ShowToast(val message: String) : UiDbState<Nothing>()
    data class Success<out T>(val uiDishItems: List<T>) : UiDbState<T>()
}
