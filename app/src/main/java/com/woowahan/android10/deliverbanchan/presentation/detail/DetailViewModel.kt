package com.woowahan.android10.deliverbanchan.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateEmptyUiDishItemUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetDetailDishUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.DetailUiState
import com.woowahan.android10.deliverbanchan.presentation.state.ExhibitionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val createEmptyUiDishItemUseCase: CreateEmptyUiDishItemUseCase,
    private val getDetailDishUseCase: GetDetailDishUseCase
) : ViewModel() {

    var currentUiDishItem = MutableStateFlow<UiDishItem>(createEmptyUiDishItemUseCase())

    private val _detailState = MutableStateFlow<DetailUiState>(DetailUiState.Init)
    val detailState: StateFlow<DetailUiState> get() = _detailState

    private val _thumbList = MutableStateFlow<List<String>>(emptyList())
    val thumbList: StateFlow<List<String>> = _thumbList

    private val _sectionList = MutableStateFlow<List<String>>(emptyList())
    val sectionList: StateFlow<List<String>> = _sectionList

    private val _itemCount = MutableStateFlow<Int>(1)
    val itemCount: StateFlow<Int> = _itemCount

    fun getDetailDishInfo() {
        viewModelScope.launch {
            getDetailDishUseCase(currentUiDishItem.value.hash).onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                showToast(exception.message.toString())
            }.flowOn(Dispatchers.IO).collect { result ->
                hideLoading()
                when (result) {
                    is BaseResult.Success -> {
                        _thumbList.value = result.data.thumbImages
                        _sectionList.value = result.data.detailSection
                        _detailState.value = DetailUiState.Success(result.data)
                    }
                    is BaseResult.Error -> _detailState.value = DetailUiState.Error(result.errorCode)
                }
            }
        }
    }

    private fun setLoading() {
        _detailState.value = DetailUiState.IsLoading(true)
    }

    private fun hideLoading() {
        _detailState.value = DetailUiState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _detailState.value = DetailUiState.ShowToast(message)
    }
}