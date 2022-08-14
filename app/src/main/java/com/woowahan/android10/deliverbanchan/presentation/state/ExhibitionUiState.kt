package com.woowahan.android10.deliverbanchan.presentation.state

import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem

sealed class ExhibitionUiState {
    object Init : ExhibitionUiState()
    data class IsLoading(val isLoading: Boolean) : ExhibitionUiState()
    data class ShowToast(val message: String) : ExhibitionUiState()
    data class Success(val uiExhibitionItems: List<UiExhibitionItem>) : ExhibitionUiState()
    data class Error(val errorCode: Int) : ExhibitionUiState()
}