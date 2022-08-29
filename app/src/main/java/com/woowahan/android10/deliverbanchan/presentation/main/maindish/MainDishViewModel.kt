package com.woowahan.android10.deliverbanchan.presentation.main.maindish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllCartInfoHashSetUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetThemeDishListUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainDishViewModel @Inject constructor(
    private val getThemeDishListUseCase: GetThemeDishListUseCase,
    private val getAllCartInfoSetUseCase: GetAllCartInfoHashSetUseCase
) : ViewModel() {

    companion object {
        const val THEME = "main"
    }

    private val _mainDishState = MutableStateFlow<UiState<List<UiDishItem>>>(UiState.Init)
    val mainDishState: StateFlow<UiState<List<UiDishItem>>> get() = _mainDishState

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
                    (_mainDishState.value as UiState.Success).items.forEach {
                        tempList.add(it.copy(isInserted = cartInfoHashSet.contains(it.hash)))
                    }
                    _mainDishState.value = UiState.Success(tempList)
                }
            }
        }
    }

    fun getMainDishList() {
        viewModelScope.launch {
            getThemeDishListUseCase(THEME).onStart {
                _mainDishState.value = UiState.Loading(true)
            }.catch { exception ->
                _mainDishState.value = UiState.Loading(false)
                _mainDishState.value = UiState.Error(exception.message.toString())
            }.collect { result ->
                _mainDishState.value = UiState.Loading(false)
                withContext(Dispatchers.Main) {
                    when (result) {
                        is BaseResult.Success -> {
                            mainDishList = result.data
                            _mainDishState.value = UiState.Success(result.data)
                        }
                        is BaseResult.Error -> {
                            _mainDishState.value = UiState.Error(result.error)
                        }
                    }
                }
            }
        }
    }

    fun sortMainDishes(position: Int) {
        if (_curMainSpinnerPosition.value != _preMainSpinnerPosition.value) { // 정렬 기준이 변경될 시
            _preMainSpinnerPosition.value = _curMainSpinnerPosition.value
        }
        _curMainSpinnerPosition.value = position
        if (_mainDishState.value is UiState.Success) {
            _mainDishState.value = when (_curMainSpinnerPosition.value) {
                1 -> UiState.Success((_mainDishState.value as UiState.Success).items.sortedBy { -it.sPrice }) // 금액 내림차순
                2 -> UiState.Success((_mainDishState.value as UiState.Success).items.sortedBy { it.sPrice }) // 금액 오름차순
                3 -> UiState.Success((_mainDishState.value as UiState.Success).items.sortedBy { -it.salePercentage }) // 할인률 내림차순
                else -> UiState.Success((_mainDishState.value as UiState.Success).items.sortedBy { it.index }) // 기본 정렬순
            }
        }
    }
}