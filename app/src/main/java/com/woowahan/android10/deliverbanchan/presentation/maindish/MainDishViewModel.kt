package com.woowahan.android10.deliverbanchan.presentation.maindish

import androidx.lifecycle.ViewModel
import com.woowahan.android10.deliverbanchan.domain.usecase.GetMainDishListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainDishViewModel @Inject constructor(
    private val getMainDishListUseCase: GetMainDishListUseCase
) : ViewModel() {

}