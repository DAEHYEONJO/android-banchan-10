package com.woowahan.android10.deliverbanchan.presentation.cart.recent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllRecentJoinPagerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    private val getAllRecentJoinPagerUseCase: GetAllRecentJoinPagerUseCase
) : ViewModel() {

    private val _recentJoinState = MutableStateFlow<PagingData<UiDishItem>>(PagingData.empty())
    val recentJoinItem: StateFlow<PagingData<UiDishItem>> get() = _recentJoinState

    companion object {
        const val TAG = "RecentViewModel"
    }

    init {
        testPagingFlow()
    }

    private fun testPagingFlow() {
        viewModelScope.launch {
            getAllRecentJoinPagerUseCase().cachedIn(viewModelScope)
                .collectLatest { recentViewedPagingData ->
                    _recentJoinState.value = recentViewedPagingData
                }
        }
    }
}