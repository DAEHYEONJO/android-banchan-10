package com.woowahan.android10.deliverbanchan.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.local.model.*
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.usecase.GetSoupDishesUseCase
import com.woowahan.android10.deliverbanchan.presentation.UiState
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

}