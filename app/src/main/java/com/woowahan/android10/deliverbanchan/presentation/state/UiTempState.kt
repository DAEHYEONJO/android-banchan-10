package com.woowahan.android10.deliverbanchan.presentation.state

sealed class UiTempState<out T> {
    object Init : UiTempState<Nothing>()
    data class Empty(val isEmpty: Boolean): UiTempState<Nothing>()
    data class Loading(val isLoading: Boolean) : UiTempState<Nothing>()
    data class ShowToast(val message: String) : UiTempState<Nothing>()
    data class Success<out T>(val items: T) : UiTempState<T>()
    data class Error(val errorCode: Int): UiTempState<Nothing>()
}
