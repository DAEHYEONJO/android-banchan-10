package com.woowahan.android10.deliverbanchan.presentation.state

sealed class UiState<out T> {
    object Init : UiState<Nothing>()
    data class Empty(val isEmpty: Boolean): UiState<Nothing>()
    data class Loading(val isLoading: Boolean) : UiState<Nothing>()
    data class ShowToast(val message: String) : UiState<Nothing>()
    data class Success<out T>(val items: T) : UiState<T>()
    data class Error(val errorCode: Int): UiState<Nothing>()
}
