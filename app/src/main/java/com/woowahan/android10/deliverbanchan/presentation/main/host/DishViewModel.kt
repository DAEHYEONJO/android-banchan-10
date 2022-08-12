package com.woowahan.android10.deliverbanchan.presentation.main.host

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.usecase.GetSoupDishesUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishViewModel @Inject constructor(
    private val getSoupDishesUseCase: GetSoupDishesUseCase
) : ViewModel() {

    companion object {
        const val TAG = "DishViewModel"
    }

    init {

    }

    private val _soupState = MutableStateFlow<UiState>(UiState.Init)
    val soupState: StateFlow<UiState> get() = _soupState
    private val _curSoupSpinnerPosition = MutableLiveData<Int>(0)
    val curSoupSpinnerPosition: LiveData<Int> get() = _curSoupSpinnerPosition
    private val _preSoupSpinnerPosition = MutableLiveData(0)
    val preSoupSpinnerPosition: LiveData<Int> get() = _preSoupSpinnerPosition

    private fun setLoading() {
        _soupState.value = UiState.IsLoading(true)
    }

    private fun hideLoading() {
        _soupState.value = UiState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _soupState.value = UiState.ShowToast(message)
    }

    fun getSoupDishes() = viewModelScope.launch {
        getSoupDishesUseCase().onStart {
            setLoading()
        }.catch { exception ->
            hideLoading()
            showToast(exception.message.toString())
        }.flowOn(Dispatchers.IO).collect { result ->
            hideLoading()
            when (result) {
                is BaseResult.Success -> _soupState.value = UiState.Success(result.data)
                is BaseResult.Error -> _soupState.value = UiState.Error(result.errorCode)
            }
        }
    }

    fun sortSoupDishes(position: Int) {
        if (_curSoupSpinnerPosition.value == _preSoupSpinnerPosition.value) {

        } else {
            _preSoupSpinnerPosition.value = _curSoupSpinnerPosition.value
        }
        _curSoupSpinnerPosition.value = position
        if (_soupState.value is UiState.Success) {
            _soupState.value = when (_curSoupSpinnerPosition.value) {
                1 -> UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { -it.sPrice }) // 금액 내림차순
                2 -> UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { it.sPrice }) // 금액 오름차순
                3 -> UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy {
                    -it.salePercentage
                }) // 할인률 내림차순
                else -> {
                    UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { it.index })
                } // 기본 정렬순
            }
        }
    }

}