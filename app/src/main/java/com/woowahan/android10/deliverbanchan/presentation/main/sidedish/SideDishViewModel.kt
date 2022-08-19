package com.woowahan.android10.deliverbanchan.presentation.main.sidedish

import androidx.lifecycle.*
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
        setSideDishCartInserted()
    }

    private fun setSideDishCartInserted() {
        // cart flow collect 시 , 장바구니 insert 여부 확인함
        viewModelScope.launch {
            getAllCartInfoSetUseCase().collect { cartInfoHashMap ->
                if (_sideState.value is UiState.Success){
                    val tempList = mutableListOf<UiDishItem>()
                    (_sideState.value as UiState.Success).uiDishItems.forEach {
                        tempList.add(it.copy(isInserted = cartInfoHashMap.contains(it.hash)))
                    }
                    _sideState.value = UiState.Success(tempList)
                }
            }
        }
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

    fun changeSoupItemIsInserted(hash: String){
        ((_sideState.value as UiState.Success).uiDishItems).let { uiDishList ->
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
                1 -> UiState.Success((_sideState.value as UiState.Success).uiDishItems.sortedBy { -it.sPrice }) // 금액 내림차순
                2 -> UiState.Success((_sideState.value as UiState.Success).uiDishItems.sortedBy { it.sPrice }) // 금액 오름차순
                3 -> UiState.Success((_sideState.value as UiState.Success).uiDishItems.sortedBy { -it.salePercentage }) // 할인률 내림차순
                else -> UiState.Success((_sideState.value as UiState.Success).uiDishItems.sortedBy { it.index }) // 기본 정렬순
            }
        }
    }

}