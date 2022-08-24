package com.woowahan.android10.deliverbanchan.presentation.main.maindish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateUiDishItemsUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllCartInfoHashSetUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainDishViewModel @Inject constructor(
    private val createUiDishItemsUseCase: CreateUiDishItemsUseCase,
    private val getAllCartInfoSetUseCase: GetAllCartInfoHashSetUseCase
) : ViewModel() {

    private val _mainDishState = MutableStateFlow<UiState>(UiState.Init)
    val mainDishState: StateFlow<UiState> get() = _mainDishState

    var mainDishList = listOf<UiDishItem>()

    private val _curMainSpinnerPosition = MutableLiveData<Int>(0)
    val curMainSpinnerPosition: LiveData<Int> get() = _curMainSpinnerPosition
    private val _preMainSpinnerPosition = MutableLiveData(0)
    val preMainSpinnerPosition: LiveData<Int> get() = _preMainSpinnerPosition
    val mainListSize = MutableLiveData(0)

    init {
        getMainDishList()
        setMainDishCartInserted()
    }

    private fun setMainDishCartInserted() { // 카트 DB 변화 시 자동 감지
        viewModelScope.launch {
            getAllCartInfoSetUseCase().collect { cartInfoHashSet ->
                if (_mainDishState.value is UiState.Success) {
                    val tempList = mutableListOf<UiDishItem>()
                    (_mainDishState.value as UiState.Success).uiDishItems.forEach {
                        tempList.add(it.copy(isInserted = cartInfoHashSet.contains(it.hash)))
                    }
                    _mainDishState.value = UiState.Success(tempList)
                }
            }
        }
    }

    fun getMainDishList() {
        Log.e("MainDishViewModel", "getMainDishList")
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

    fun sortMainDishes(position: Int) {
        if (_curMainSpinnerPosition.value != _preMainSpinnerPosition.value) { // 정렬 기준이 변경될 시
            _preMainSpinnerPosition.value = _curMainSpinnerPosition.value
        }
        _curMainSpinnerPosition.value = position
        if (_mainDishState.value is UiState.Success) {
            _mainDishState.value = when (_curMainSpinnerPosition.value) {
                1 -> UiState.Success((_mainDishState.value as UiState.Success).uiDishItems.sortedBy { -it.sPrice }) // 금액 내림차순
                2 -> UiState.Success((_mainDishState.value as UiState.Success).uiDishItems.sortedBy { it.sPrice }) // 금액 오름차순
                3 -> UiState.Success((_mainDishState.value as UiState.Success).uiDishItems.sortedBy { -it.salePercentage }) // 할인률 내림차순
                else -> UiState.Success((_mainDishState.value as UiState.Success).uiDishItems.sortedBy { it.index }) // 기본 정렬순
            }
        }
    }
}