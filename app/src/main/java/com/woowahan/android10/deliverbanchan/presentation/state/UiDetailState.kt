package com.woowahan.android10.deliverbanchan.presentation.state

import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo

sealed class UiDetailState {
    object Init : UiDetailState()
    data class Loading(val isLoading: Boolean) : UiDetailState()
    data class ShowToast(val message: String) : UiDetailState()
    data class Success(val uiDishItems: UiDetailInfo) : UiDetailState()
    data class Error(val errorCode: Int) : UiDetailState()
}