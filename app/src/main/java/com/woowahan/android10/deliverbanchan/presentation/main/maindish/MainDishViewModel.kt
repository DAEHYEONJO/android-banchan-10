package com.woowahan.android10.deliverbanchan.presentation.main.maindish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllCartInfoHashSetUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetThemeDishListUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiTempState
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

    companion object{
        const val THEME = "main"
    }

    private val _mainDishState = MutableStateFlow<UiTempState<List<UiDishItem>>>(UiTempState.Init)
    val mainDishState: StateFlow<UiTempState<List<UiDishItem>>> get() = _mainDishState

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
                if (_mainDishState.value is UiTempState.Success) {
                    val tempList = mutableListOf<UiDishItem>()
                    (_mainDishState.value as UiTempState.Success).items.forEach {
                        tempList.add(it.copy(isInserted = cartInfoHashSet.contains(it.hash)))
                    }
                    _mainDishState.value = UiTempState.Success(tempList)
                }
            }
        }
    }

    fun getMainDishList() {
        Log.e("MainDishViewModel", "getMainDishList")
        viewModelScope.launch {
            getThemeDishListUseCase(THEME).onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                Log.e("MainDishViewModel", "exception : ${exception.message}")
                showToast(exception.message.toString())
                catchError(1)
            }.collect { result ->
                hideLoading()
                withContext(Dispatchers.Main) {
                    when (result) {
                        is BaseResult.Success -> {
                            mainDishList = result.data
                            _mainDishState.value = UiTempState.Success(result.data)
                        }
                        is BaseResult.Error -> catchError(1)
                    }
                }
            }
        }
    }

    private fun setLoading() {
        _mainDishState.value = UiTempState.Loading(true)
    }

    private fun hideLoading() {
        _mainDishState.value = UiTempState.Loading(false)
    }

    private fun showToast(message: String) {
        _mainDishState.value = UiTempState.ShowToast(message)
    }

    private fun catchError(errorCode: Int) {
        _mainDishState.value = UiTempState.Error(errorCode)
    }

    fun sortMainDishes(position: Int) {
        Log.e("MainDishViewModel", "sortMainDishes: pre: ${_preMainSpinnerPosition.value} cur: ${_curMainSpinnerPosition.value}", )
        if (_curMainSpinnerPosition.value != _preMainSpinnerPosition.value) { // 정렬 기준이 변경될 시
            _preMainSpinnerPosition.value = _curMainSpinnerPosition.value
        }
        _curMainSpinnerPosition.value = position
        if (_mainDishState.value is UiTempState.Success) {
            _mainDishState.value = when (_curMainSpinnerPosition.value) {
                1 -> UiTempState.Success((_mainDishState.value as UiTempState.Success).items.sortedBy { -it.sPrice }) // 금액 내림차순
                2 -> UiTempState.Success((_mainDishState.value as UiTempState.Success).items.sortedBy { it.sPrice }) // 금액 오름차순
                3 -> UiTempState.Success((_mainDishState.value as UiTempState.Success).items.sortedBy { -it.salePercentage }) // 할인률 내림차순
                else -> UiTempState.Success((_mainDishState.value as UiTempState.Success).items.sortedBy { it.index }) // 기본 정렬순
            }
        }
    }
}