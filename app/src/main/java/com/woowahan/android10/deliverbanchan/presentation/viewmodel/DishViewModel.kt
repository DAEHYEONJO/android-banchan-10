package com.woowahan.android10.deliverbanchan.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.usecase.GetSoupDishesUseCase
import com.woowahan.android10.deliverbanchan.presentation.view.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishViewModel @Inject constructor(
    private val getSoupDishesUseCase: GetSoupDishesUseCase
) : ViewModel() {

    companion object{
        const val TAG = "DishViewModel"
    }

    init {

    }

    private val _soupState = MutableStateFlow<UiState>(UiState.Init)
    val soupState: StateFlow<UiState> get() = _soupState
    private val _soupSpinnerPosition = MutableLiveData<Int>(0)
    val soupSpinnerPosition: LiveData<Int> get() = _soupSpinnerPosition

    private fun setLoading(){
        _soupState.value = UiState.IsLoading(true)
    }
    private fun hideLoading(){
        _soupState.value = UiState.IsLoading(false)
    }
    private fun showToast(message: String){
        _soupState.value = UiState.ShowToast(message)
    }

    fun getSoupDishes() = viewModelScope.launch {
        getSoupDishesUseCase().onStart {
            setLoading()
        }.catch { exception ->
            hideLoading()
            showToast(exception.message.toString())
        }.collect{ result ->
            hideLoading()
            when(result){
                is BaseResult.Success -> _soupState.value = UiState.Success(result.data)
                is BaseResult.Error -> _soupState.value = UiState.Error(result.errorCode)
            }
        }
    }

    fun sortSoupDishes(position: Int){
        _soupSpinnerPosition.value = position
        if (_soupState.value is UiState.Success){
            _soupState.value = when(position){
                1 -> UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { -it.sPrice })
                2 -> UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { it.sPrice })
                3 -> UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { -(it.salePercentage.dropLast(1).toInt()) })
                else -> {UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { it.index })}
            }
        }
    }

}