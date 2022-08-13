package com.woowahan.android10.deliverbanchan.presentation.main.host

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.domain.usecase.GetAllCartInfoUseCase
import com.woowahan.android10.deliverbanchan.presentation.state.UiCartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishViewModel @Inject constructor(
    private val getAllCartInfoUseCase: GetAllCartInfoUseCase
) : ViewModel() {

    companion object {
        const val TAG = "DishViewModel"
    }
    private val _cartInfoState = MutableStateFlow<UiCartState>(UiCartState.Init)
    val cartInfoState: StateFlow<UiCartState> get() = _cartInfoState
    init {
        getAllCartInfo()
    }

    private fun getAllCartInfo() = viewModelScope.launch {
        getAllCartInfoUseCase().catch { exception ->
            _cartInfoState.value = UiCartState.ShowToast(exception.message.toString())
        }.collect{
            _cartInfoState.value = UiCartState.Success(it)
        }
    }
}