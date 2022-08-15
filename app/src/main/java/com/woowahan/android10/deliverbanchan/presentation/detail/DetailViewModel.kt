package com.woowahan.android10.deliverbanchan.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateEmptyUiDishItemUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetDetailDishUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val createEmptyUiDishItemUseCase: CreateEmptyUiDishItemUseCase,
    private val getDetailDishUseCase: GetDetailDishUseCase
) : ViewModel() {

    var currentUiDishItem = MutableStateFlow<UiDishItem>(createEmptyUiDishItemUseCase())

    private val _thumbList = MutableStateFlow<List<String>>(emptyList())
    val thumbList: StateFlow<List<String>> = _thumbList

    private val _sectionList = MutableStateFlow<List<String>>(emptyList())
    val sectionList: StateFlow<List<String>> = _sectionList

    fun getDetailDishInfo() {
        viewModelScope.launch {
            
        }
    }

}