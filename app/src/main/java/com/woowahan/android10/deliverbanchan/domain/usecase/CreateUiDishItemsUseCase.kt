package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateUiDishItemsUseCase @Inject constructor(
    private val getDishListByThemeUseCase: GetDishListByThemeUseCase,
    private val mapDishItemListToUiDishItemListUseCase: MapDishItemListToUiDishItemListUseCase,
) {
    suspend operator fun invoke(theme: String): Flow<BaseResult<List<UiDishItem>, Int>> {
        return getDishListByThemeUseCase(theme).map { response ->
            when (response) {
                is BaseResult.Success -> {
                    val resultUiDishItemList = mapDishItemListToUiDishItemListUseCase(response.data)
                    BaseResult.Success(resultUiDishItemList)
                }
                is BaseResult.Error -> {
                    BaseResult.Error(errorCode = response.errorCode)
                }
            }
        }
    }
}