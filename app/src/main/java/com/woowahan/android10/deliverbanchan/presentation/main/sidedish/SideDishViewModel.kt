package com.woowahan.android10.deliverbanchan.presentation.main.sidedish

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.usecase.GetSideDishListUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideDishViewModel @Inject constructor(
    private val getSideDishListUseCase: GetSideDishListUseCase
): ViewModel(){

    private val _sideState = MutableStateFlow<UiState>(UiState.Init)
    val sideState: StateFlow<UiState> get() = _sideState
    val sideListSize = MutableLiveData(0)

    private fun setLoading() {
        _sideState.value = UiState.IsLoading(true)
    }

    private fun hideLoading() {
        _sideState.value = UiState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _sideState.value = UiState.ShowToast(message)
    }

    init {
        getSideDishList()
    }

    private fun getSideDishList() = viewModelScope.launch {
        getSideDishListUseCase().onStart {
            setLoading()
        }.catch { exception ->
            hideLoading()
            showToast(exception.message.toString())
        }.flowOn(Dispatchers.IO).collect { result ->
            hideLoading()
            when (result) {
                is BaseResult.Success -> {
                    _sideState.value = UiState.Success(result.data)
                    sideListSize.value = result.data.size
                }
                is BaseResult.Error -> _sideState.value = UiState.Error(result.errorCode)
            }
        }
    }
}