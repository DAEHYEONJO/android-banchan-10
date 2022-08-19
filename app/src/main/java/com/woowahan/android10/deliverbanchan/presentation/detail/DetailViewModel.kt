package com.woowahan.android10.deliverbanchan.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.*
import com.woowahan.android10.deliverbanchan.presentation.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val createEmptyUiDishItemUseCase: CreateEmptyUiDishItemUseCase,
    private val createEmptyUiDetailInfoUseCase: CreateEmptyUiDetailInfoUseCase,
    private val createUiDetailInfoUseCase: CreateUiDetailInfoUseCase,
    private val getDetailDishUseCase: GetDetailDishUseCase,
    private val insertRecentlyUseCase: InsertRecentlyUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentUiDishItem: UiDishItem? = savedStateHandle["UiDishItem"]

    private val _detailState = MutableStateFlow<DetailUiState>(DetailUiState.Init)
    val detailState: StateFlow<DetailUiState> get() = _detailState

    private val _thumbList = MutableStateFlow<List<String>>(emptyList())
    val thumbList: StateFlow<List<String>> = _thumbList

    private val _sectionList = MutableStateFlow<List<String>>(emptyList())
    val sectionList: StateFlow<List<String>> = _sectionList

    private val _uiDetailInfo = MutableStateFlow<UiDetailInfo>(createEmptyUiDetailInfoUseCase())
    val uiDetailInfo: StateFlow<UiDetailInfo> = _uiDetailInfo

    private val _itemCount = MutableStateFlow<Int>(1)
    val itemCount: StateFlow<Int> = _itemCount

    init {
        getDetailDishInfo()
    }

    private fun insertRecently() {
        currentUiDishItem?.let { dishItem ->
            viewModelScope.launch {
                with(dishItem) {
                    insertRecentlyUseCase(
                        LocalDish(
                            hash, title, image, nPrice, sPrice
                        ),
                        RecentViewedInfo(
                            hash, System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }

    private fun getDetailDishInfo() {
        viewModelScope.launch {
            getDetailDishUseCase(currentUiDishItem!!.hash).onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                showToast(exception.message.toString())
            }.flowOn(Dispatchers.IO).collect { result ->
                hideLoading()
                when (result) {
                    is BaseResult.Success -> {
                        _thumbList.value = result.data.thumbImages
                        _sectionList.value = result.data.detailSection
                        _uiDetailInfo.value = createUiDetailInfoUseCase(
                            currentUiDishItem,
                            result.data,
                            _itemCount.value
                        )
                        _detailState.value = DetailUiState.Success(result.data)
                        insertRecently()
                    }
                    is BaseResult.Error -> _detailState.value =
                        DetailUiState.Error(result.errorCode)
                }
            }
        }
    }

    fun plusItemCount() {
        _itemCount.value += 1
        _uiDetailInfo.value = _uiDetailInfo.value.copy(itemCount = _itemCount.value)
    }

    fun minusItemCount() {
        if (_itemCount.value >= 2) {
            _itemCount.value -= 1
            _uiDetailInfo.value = _uiDetailInfo.value.copy(itemCount = _itemCount.value)
        }
    }

    private fun setLoading() {
        _detailState.value = DetailUiState.IsLoading(true)
    }

    private fun hideLoading() {
        _detailState.value = DetailUiState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _detailState.value = DetailUiState.ShowToast(message)
    }
}