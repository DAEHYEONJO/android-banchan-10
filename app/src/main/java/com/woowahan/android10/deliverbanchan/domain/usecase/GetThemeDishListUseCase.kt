package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.common.convertPriceToInt
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetThemeDishListUseCase @Inject constructor(
    private val dishItemRepository: DishItemRepository,
    private val mapDishItemListToUiDishItemListUseCase: MapDishItemListToUiDishItemListUseCase
) {

    companion object {
        const val TAG = "GetThemeDishListUseCase"
    }

    suspend operator fun invoke(theme: String): Flow<BaseResult<List<UiDishItem>, Int>> {
            return dishItemRepository.getDishesByTheme(theme).map { response ->
                when (response) {
                    is BaseResult.Success -> {
                        BaseResult.Success(mapDishItemListToUiDishItemListUseCase(response.data))
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(errorCode = response.errorCode)
                    }
                }
            }
        }
    }