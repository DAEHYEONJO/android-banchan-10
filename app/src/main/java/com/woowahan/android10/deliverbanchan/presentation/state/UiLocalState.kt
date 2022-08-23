package com.woowahan.android10.deliverbanchan.presentation.state

sealed class UiLocalState<out T> {
    object Init : UiLocalState<Nothing>()
    data class Empty(val isEmpty: Boolean): UiLocalState<Nothing>()
    data class Loading(val isLoading: Boolean) : UiLocalState<Nothing>()
    data class ShowToast(val message: String) : UiLocalState<Nothing>()
    data class Success<out T>(val uiDishItems: List<T>) : UiLocalState<T>()
    data class Error(val errorCode: Int): UiLocalState<Nothing>()
}
