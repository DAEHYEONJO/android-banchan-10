package com.woowahan.android10.deliverbanchan.presentation.cart.recent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.woowahan.android10.deliverbanchan.domain.model.UiRecentJoinItem
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllRecentJoinPagerUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    private val getAllRecentJoinPagerUseCase: GetAllRecentJoinPagerUseCase
) : ViewModel() {

    private val _recentJoinState = MutableLiveData<PagingData<UiRecentJoinItem>>()
    val recentJoinItem: LiveData<PagingData<UiRecentJoinItem>> get() = _recentJoinState

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