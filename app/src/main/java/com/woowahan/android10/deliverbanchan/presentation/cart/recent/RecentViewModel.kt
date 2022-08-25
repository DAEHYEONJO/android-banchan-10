package com.woowahan.android10.deliverbanchan.presentation.cart.recent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllRecentJoinPagerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    private val getAllRecentJoinPagerUseCase: GetAllRecentJoinPagerUseCase
) : ViewModel() {

    private val _recentJoinState = MutableStateFlow<PagingData<UiDishItem>>(PagingData.empty())
    val recentJoinItem: StateFlow<PagingData<UiDishItem>> get() = _recentJoinState
    companion object{
        const val TAG = "RecentViewModel"
    }
    init {
        testPagingFlow()
    }

//    fun getPagingFlow(): Flow<PagingData<UiDishItem>> {
//        return getAllRecentJoinPagerUseCase().cachedIn(viewModelScope)
//    }
    private fun testPagingFlow() {
        viewModelScope.launch {
            getAllRecentJoinPagerUseCase().cachedIn(viewModelScope)
                .collectLatest { recentViewedPagingData ->
                    Log.e(TAG, "testPagingFlow: $recentViewedPagingData", )
                    _recentJoinState.value = recentViewedPagingData
                }
        }
    }
}