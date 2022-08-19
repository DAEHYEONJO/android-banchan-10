package com.woowahan.android10.deliverbanchan.presentation.main.maindish

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateUiDishItemsUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainDishViewModel @Inject constructor(
    private val createUiDishItemsUseCase: CreateUiDishItemsUseCase
) : ViewModel() {

    private val _mainDishState = MutableStateFlow<UiState>(UiState.Init)
    val mainDishState: StateFlow<UiState> get() = _mainDishState

    var mainDishList = listOf<UiDishItem>()

    fun getMainDishList() {
        viewModelScope.launch {
            createUiDishItemsUseCase("main").onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                Log.e("MainDishViewModel", "exception : ${exception.message}")
                showToast(exception.message.toString())
            }.collect { result ->
                hideLoading()
                withContext(Dispatchers.Main) {
                    when (result) {
                        is BaseResult.Success -> {
                            mainDishList = result.data
                            _mainDishState.value = UiState.Success(result.data)
                        }
                        is BaseResult.Error -> _mainDishState.value =
                            UiState.Error(result.errorCode)
                    }
                }
            }
        }
    }

    private fun setLoading() {
        _mainDishState.value = UiState.IsLoading(true)
    }

    private fun hideLoading() {
        _mainDishState.value = UiState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _mainDishState.value = UiState.ShowToast(message)
    }

    fun changeMainDishItemIsInserted(hash: String){
        ((_mainDishState.value as UiState.Success).uiDishItems).let { uiDishList ->
            val newList = mutableListOf<UiDishItem>().apply {
                uiDishList.forEach { uiDishItem ->
                    if (uiDishItem.hash == hash){
                        add(uiDishItem.copy(isInserted = true))
                    }else{
                        add(uiDishItem)
                    }
                }
            }
            mainDishList = newList
            _mainDishState.value = UiState.Success(newList)
        }
    }


}