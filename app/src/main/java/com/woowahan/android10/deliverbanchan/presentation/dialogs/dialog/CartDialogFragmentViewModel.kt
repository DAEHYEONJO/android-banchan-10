package com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog

import androidx.lifecycle.ViewModel
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateEmptyUiDishItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CartDialogFragmentViewModel @Inject constructor() : ViewModel() {
    var currentHash = ""
    var currentTitle = ""
}