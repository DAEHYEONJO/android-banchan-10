package com.woowahan.android10.deliverbanchan.presentation.maindish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateEmptyUiDishItemUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.CreateUiDishItemUseCase
import com.woowahan.android10.deliverbanchan.domain.usecase.GetMainDishListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainDishViewModel @Inject constructor(
    private val getMainDishListUseCase: GetMainDishListUseCase,
    private val createUiDishItemUseCase: CreateUiDishItemUseCase,
    private val createEmptyUiDishItemUseCase: CreateEmptyUiDishItemUseCase
) : ViewModel() {

    private val _mainDishListFlow = MutableStateFlow<List<UiDishItem>>(emptyList())
    val mainDishListFlow: StateFlow<List<UiDishItem>> = _mainDishListFlow

    fun getMainDishList() {
        viewModelScope.launch {
            getMainDishListUseCase.invoke()
                .onStart {
                    // Loading 처리
                }.collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            val resultUiDishItemList =
                                MutableList<UiDishItem>(result.data.size) { createEmptyUiDishItemUseCase() }
                            result.data.mapIndexed { index, dishItem ->
                                async {
                                    resultUiDishItemList[index] = createUiDishItemUseCase(dishItem)
                                }
                            }.awaitAll()
                            _mainDishListFlow.value = resultUiDishItemList
                        }
                        is BaseResult.Error -> {
                            // Error 처리
                        }
                    }
                }
        }
    }

}