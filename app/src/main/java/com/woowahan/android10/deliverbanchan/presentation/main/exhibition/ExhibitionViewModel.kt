package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateUiExhibitionItemsUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.ExhibitionUiState
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExhibitionViewModel @Inject constructor(
    private val createUiExhibitionItemsUseCase: CreateUiExhibitionItemsUseCase
) : ViewModel() {

    private val _exhibitionState = MutableStateFlow<ExhibitionUiState>(ExhibitionUiState.Init)
    val exhibitionState: StateFlow<ExhibitionUiState> get() = _exhibitionState

    var exhibitionList = listOf<UiExhibitionItem>()

    init {
        getExhibitionList()
    }

    fun getExhibitionList() {
        viewModelScope.launch {
            createUiExhibitionItemsUseCase().onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                showToast(exception.message.toString())
            }.flowOn(Dispatchers.IO).collect { result ->
                hideLoading()
                withContext(Dispatchers.Main) {
                    when (result) {
                        is BaseResult.Success -> {
                            exhibitionList = result.data
                            _exhibitionState.value = ExhibitionUiState.Success(result.data)
                        }
                        is BaseResult.Error -> _exhibitionState.value =
                            ExhibitionUiState.Error(result.errorCode)
                    }
                }
            }
        }
    }

    private fun setLoading() {
        _exhibitionState.value = ExhibitionUiState.IsLoading(true)
    }

    private fun hideLoading() {
        _exhibitionState.value = ExhibitionUiState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _exhibitionState.value = ExhibitionUiState.ShowToast(message)
    }
}