package com.woowahan.android10.deliverbanchan.presentation.main.sidedish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.usecase.GetThemeDishListUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideDishViewModel @Inject constructor(
    private val getSideDishListUseCase: GetThemeDishListUseCase
): ViewModel(){

    private val _sideState = MutableStateFlow<UiState>(UiState.Init)
    val sideState: StateFlow<UiState> get() = _sideState
    val sideListSize = MutableLiveData(0)
    private val _curSideDishSpinnerPosition = MutableLiveData<Int>(0)
    val curSideDishSpinnerPosition: LiveData<Int> get() = _curSideDishSpinnerPosition
    private val _preSideDishSpinnerPosition = MutableLiveData(0)
    val preSideDishSpinnerPosition: LiveData<Int> get() = _preSideDishSpinnerPosition

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
        getSideDishListUseCase("side").onStart {
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


    fun sortSoupDishes(position: Int) {
        if (_curSideDishSpinnerPosition.value != _preSideDishSpinnerPosition.value) {
            _preSideDishSpinnerPosition.value = _curSideDishSpinnerPosition.value
        }
        _curSideDishSpinnerPosition.value = position
        if (_sideState.value is UiState.Success) {
            _sideState.value = when (_curSideDishSpinnerPosition.value) {
                1 -> UiState.Success((_sideState.value as UiState.Success).uiDishItems.sortedBy { -it.sPrice }) // 금액 내림차순
                2 -> UiState.Success((_sideState.value as UiState.Success).uiDishItems.sortedBy { it.sPrice }) // 금액 오름차순
                3 -> UiState.Success((_sideState.value as UiState.Success).uiDishItems.sortedBy { -it.salePercentage }) // 할인률 내림차순
                else -> UiState.Success((_sideState.value as UiState.Success).uiDishItems.sortedBy { it.index }) // 기본 정렬순
            }
        }
    }

}