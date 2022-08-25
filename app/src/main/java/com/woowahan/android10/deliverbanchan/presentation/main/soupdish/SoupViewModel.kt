package com.woowahan.android10.deliverbanchan.presentation.main.soupdish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllCartInfoHashSetUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.remote.GetThemeDishListUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoupViewModel @Inject constructor(
    private val getSoupDishesUseCase: GetThemeDishListUseCase,
    private val getAllCartInfoSetUseCase: GetAllCartInfoHashSetUseCase
) : ViewModel() {

    companion object {
        const val TAG = "SoupViewModel"
    }

    private val _soupState = MutableStateFlow<UiState>(UiState.Init)
    val soupState: StateFlow<UiState> get() = _soupState
    private val _curSoupSpinnerPosition = MutableLiveData<Int>(0)
    val curSoupSpinnerPosition: LiveData<Int> get() = _curSoupSpinnerPosition
    private val _preSoupSpinnerPosition = MutableLiveData(0)
    val preSoupSpinnerPosition: LiveData<Int> get() = _preSoupSpinnerPosition
    val soupListSize = MutableLiveData(0)

    init {
        setSoupDishesState()
        setSoupDishCartInserted()
    }

    private fun setSoupDishCartInserted() {
        // cart flow collect 시 , 장바구니 insert 여부 확인함
        viewModelScope.launch {
            getAllCartInfoSetUseCase().collect { cartInfoHashSet ->
                if (_soupState.value is UiState.Success) {
                    val tempList = mutableListOf<UiDishItem>()
                    (_soupState.value as UiState.Success).uiDishItems.forEach {
                        tempList.add(it.copy(isInserted = cartInfoHashSet.contains(it.hash)))
                    }
                    _soupState.value = UiState.Success(tempList)
                }
            }
        }
    }

    private fun setLoadingStateTrue() {
        _soupState.value = UiState.IsLoading(true)
    }

    private fun setLoadingStateFalse() {
        _soupState.value = UiState.IsLoading(false)
    }

    private fun setToastMessageByException(message: String) {
        _soupState.value = UiState.ShowToast(message)
    }

    private fun catchError(errorCode: Int) {
        _soupState.value = UiState.Error(errorCode)
    }

    fun setSoupDishesState() {
        viewModelScope.launch {
            Log.e("SoupViewModel", "getSoupList")
            getSoupDishesUseCase("soup").onStart {
                setLoadingStateTrue()
            }.catch { exception ->
                Log.e(TAG, "getSoupDishes: $exception")
                setLoadingStateFalse()
                setToastMessageByException(exception.message.toString())
                catchError(1)
            }.flowOn(Dispatchers.IO).collect { result ->
                setLoadingStateFalse()
                when (result) {
                    is BaseResult.Success -> {
                        _soupState.value = UiState.Success(result.data)
                        soupListSize.value = result.data.size
                    }
                    is BaseResult.Error -> catchError(result.errorCode)
                }
            }
        }
    }

    fun sortSoupDishes(position: Int) {
        if (_curSoupSpinnerPosition.value != _preSoupSpinnerPosition.value) {
            _preSoupSpinnerPosition.value = _curSoupSpinnerPosition.value
        }
        _curSoupSpinnerPosition.value = position
        if (_soupState.value is UiState.Success) {
            _soupState.value = when (_curSoupSpinnerPosition.value) {
                1 -> UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { -it.sPrice }) // 금액 내림차순
                2 -> UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { it.sPrice }) // 금액 오름차순
                3 -> UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { -it.salePercentage }) // 할인률 내림차순
                else -> UiState.Success((_soupState.value as UiState.Success).uiDishItems.sortedBy { it.index }) // 기본 정렬순
            }
        }
    }
}