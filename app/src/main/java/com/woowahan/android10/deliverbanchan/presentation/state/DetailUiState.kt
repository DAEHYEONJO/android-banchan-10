package com.woowahan.android10.deliverbanchan.presentation.state

import com.woowahan.android10.deliverbanchan.data.remote.model.DishDetail

sealed class DetailUiState {
    object Init : DetailUiState()
    data class IsLoading(val isLoading: Boolean) : DetailUiState()
    data class ShowToast(val message: String) : DetailUiState()
    data class Success(val uiDishItems: DishDetail.DishDetailData) : DetailUiState()
    data class Error(val errorCode: Int) : DetailUiState()
}