package com.woowahan.android10.deliverbanchan.presentation.order

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.local.model.join.Order
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderListItem
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllOrderJoinListUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.InsertOrderInfoUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val insertOrderInfoUseCase: InsertOrderInfoUseCase,
    private val getAllOrderJoinListUseCase: GetAllOrderJoinListUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        const val TAG = "OrderViewModel"
    }

    val appBarTitle = MutableLiveData("")
    val orderDetailMode = MutableLiveData(false)
    val currentFragmentName = MutableStateFlow<String>("OrderList")

    private val _allOrderJoinState =
        MutableStateFlow<UiLocalState<UiOrderListItem>>(UiLocalState.Init)
    val allOrderJoinState: StateFlow<UiLocalState<UiOrderListItem>> get() = _allOrderJoinState

    init {
        getAllOrderList()
    }

    fun tempInsertOrderInfo() { // 지울 예정
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            runCatching {
                // insertOrderInfoUseCase 활용해서 테스트 데이터 넣기
            }.onSuccess {
                Log.e(TAG, "temp order info insert success")
            }.onFailure {
                Log.e(TAG, "temp order info insert error")
            }
        }
    }

    fun getAllOrderList() {
        viewModelScope.launch {
            getAllOrderJoinListUseCase().onStart {
                _allOrderJoinState.value = UiLocalState.IsLoading(true)
            }.flowOn(dispatcher).catch { exception ->
                _allOrderJoinState.value = UiLocalState.IsLoading(false)
                _allOrderJoinState.value = UiLocalState.ShowToast(exception.message.toString())
            }.collect {
                _allOrderJoinState.value = UiLocalState.IsLoading(false)
                if (it.isEmpty()) _allOrderJoinState.value = UiLocalState.IsEmpty(true)
                else {
                    val map = it.reversed().groupBy { it.timeStamp }
                    val list = map.toList().map {
                        UiOrderListItem(
                            it.first,
                            it.second
                        )
                    }
                    _allOrderJoinState.value = UiLocalState.Success(list)

                }
            }
        }
    }

    fun setAppBarTitle(string: String) {
        appBarTitle.value = string
    }
}