package com.woowahan.android10.deliverbanchan.presentation.main.sidedish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllCartInfoHashSetUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetThemeDishListUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideDishViewModel @Inject constructor(
    private val getSideDishListUseCase: GetThemeDishListUseCase,
    private val getAllCartInfoSetUseCase: GetAllCartInfoHashSetUseCase
): ViewModel(){

    companion object{
        const val THEME = "side"
    }

    private val _sideState = MutableStateFlow<UiState<List<UiDishItem>>>(UiState.Init)
    val sideState: StateFlow<UiState<List<UiDishItem>>> get() = _sideState
    val sideListSize = MutableLiveData(0)
    private val _curSideDishSpinnerPosition = MutableLiveData<Int>(0)
    val curSideDishSpinnerPosition: LiveData<Int> get() = _curSideDishSpinnerPosition
    private val _preSideDishSpinnerPosition = MutableLiveData(0)
    val preSideDishSpinnerPosition: LiveData<Int> get() = _preSideDishSpinnerPosition

    private fun setLoading() {
        _sideState.value = UiState.Loading(true)
    }

    private fun hideLoading() {
        _sideState.value = UiState.Loading(false)
    }

    private fun showToast(message: String) {
        _sideState.value = UiState.ShowToast(message)
    }

    private fun catchError(errorCode: Int) {
        _sideState.value = UiState.Error(errorCode)
    }

    init {
        getSideDishList()
        setSideDishCartInserted()
    }

    private fun setSideDishCartInserted() {
        // cart flow collect 시 , 장바구니 insert 여부 확인함
        viewModelScope.launch {
            getAllCartInfoSetUseCase().collect { cartInfoHashMap ->
                if (_sideState.value is UiState.Success){
                    val tempList = mutableListOf<UiDishItem>()
                    (_sideState.value as UiState.Success).items.forEach {
                        tempList.add(it.copy(isInserted = cartInfoHashMap.contains(it.hash)))
                    }
                    _sideState.value = UiState.Success(tempList)
                }
            }
        }
    }

    fun getSideDishList() = viewModelScope.launch {
        Log.e("SideDishViewModel", "getSideDishList")
        getSideDishListUseCase(THEME).onStart {
            setLoading()
        }.catch { exception ->
            hideLoading()
            showToast(exception.message.toString())
            catchError(1)
        }.flowOn(Dispatchers.IO).collect { result ->
            hideLoading()
            when (result) {
                is BaseResult.Success -> {
                    _sideState.value = UiState.Success(result.data)
                    sideListSize.value = result.data.size
                }
                is BaseResult.Error -> catchError(result.errorCode)
            }
        }
    }

    fun changeSoupItemIsInserted(hash: String){
        ((_sideState.value as UiState.Success).items).let { uiDishList ->
            val newList = mutableListOf<UiDishItem>().apply {
                uiDishList.forEach { uiDishItem ->
                    if (uiDishItem.hash == hash){
                        add(uiDishItem.copy(isInserted = true))
                    }else{
                        add(uiDishItem)
                    }
                }
            }
            _sideState.value = UiState.Success(newList)
        }
    }

    fun sortSoupDishes(position: Int) {
        if (_curSideDishSpinnerPosition.value != _preSideDishSpinnerPosition.value) {
            _preSideDishSpinnerPosition.value = _curSideDishSpinnerPosition.value
        }
        _curSideDishSpinnerPosition.value = position
        if (_sideState.value is UiState.Success) {
            _sideState.value = when (_curSideDishSpinnerPosition.value) {
                1 -> UiState.Success((_sideState.value as UiState.Success).items.sortedBy { -it.sPrice }) // 금액 내림차순
                2 -> UiState.Success((_sideState.value as UiState.Success).items.sortedBy { it.sPrice }) // 금액 오름차순
                3 -> UiState.Success((_sideState.value as UiState.Success).items.sortedBy { -it.salePercentage }) // 할인률 내림차순
                else -> UiState.Success((_sideState.value as UiState.Success).items.sortedBy { it.index }) // 기본 정렬순
            }
        }
    }

}