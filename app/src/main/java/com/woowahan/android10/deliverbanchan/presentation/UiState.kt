package com.woowahan.android10.deliverbanchan.presentation

import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem

sealed class UiState  {
    object Init : UiState()
    data class IsLoading(val isLoading: Boolean) : UiState()
    data class ShowToast(val message: String) : UiState()
    data class Success(val uiDishItems: List<UiDishItem>) : UiState()
    data class Error(val errorCode: Int) : UiState()
}