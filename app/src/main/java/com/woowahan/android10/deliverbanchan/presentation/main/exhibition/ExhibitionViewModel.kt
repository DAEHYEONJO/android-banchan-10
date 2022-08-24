package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateUiExhibitionItemsUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllCartInfoHashSetUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.ExhibitionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExhibitionViewModel @Inject constructor(
    private val createUiExhibitionItemsUseCase: CreateUiExhibitionItemsUseCase,
    private val getAllCartInfoSetUseCase: GetAllCartInfoHashSetUseCase
) : ViewModel() {

    private val _exhibitionState = MutableStateFlow<ExhibitionUiState>(ExhibitionUiState.Init)
    val exhibitionState: StateFlow<ExhibitionUiState> get() = _exhibitionState

    var exhibitionList = listOf<UiExhibitionItem>()

    init {
        getExhibitionList()
        setExhibitionCartInserted()
    }

    fun getExhibitionList() {
        viewModelScope.launch {
            Log.e("ExhibitionViewModel", "getExhibitionList")
            createUiExhibitionItemsUseCase().onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                showToast(exception.message.toString())
                catchError(1)
                Log.e("ExhibitionViewModel", "error catch!")
            }.flowOn(Dispatchers.IO).collect { result ->
                hideLoading()
                withContext(Dispatchers.Main) {
                    when (result) {
                        is BaseResult.Success -> {
                            exhibitionList = result.data
                            _exhibitionState.value = ExhibitionUiState.Success(result.data)
                        }
                        is BaseResult.Error -> catchError(result.errorCode)
                    }
                }
            }
        }
    }

    private fun setExhibitionCartInserted() { // 카트 DB 변화 시 자동 감지
        viewModelScope.launch {
            getAllCartInfoSetUseCase().collect { cartInfoHashSet ->
                if (_exhibitionState.value is ExhibitionUiState.Success) {
                    val tempList = mutableListOf<UiExhibitionItem>()
                    (_exhibitionState.value as ExhibitionUiState.Success).uiExhibitionItems.forEach { uiExhibitionDishItem ->
                        val newUiDishItemList = mutableListOf<UiDishItem>().apply {
                            uiExhibitionDishItem.uiDishItems.forEach { uiDishItem ->
                                add(uiDishItem.copy(isInserted = cartInfoHashSet.contains(uiDishItem.hash)))
                            }
                        }
                        tempList.add(uiExhibitionDishItem.copy(uiDishItems = newUiDishItemList))
                    }
                    _exhibitionState.value = ExhibitionUiState.Success(tempList)
                }
            }
        }
    }

    private fun setLoading() {
        _exhibitionState.value = ExhibitionUiState.IsLoading(true)
    }

    private fun hideLoading() {
        _exhibitionState.value = ExhibitionUiState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _exhibitionState.value = ExhibitionUiState.ShowToast(message)
    }

    private fun catchError(errorCode: Int) { // 함수명 더 나은것이 있을까..
        _exhibitionState.value = ExhibitionUiState.Error(errorCode)
    }
}