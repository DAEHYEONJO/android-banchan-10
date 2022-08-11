package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import com.woowahan.android10.deliverbanchan.presentation.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSoupDishesUseCase @Inject constructor(
    private val dishItemRepository: DishItemRepository
) {
    companion object {
        const val TAG = "GetSoupDishesUseCase"
    }

    suspend operator fun invoke(): Flow<BaseResult<List<UiDishItem>, Int>> {
        return dishItemRepository.getSoupDishes().map { response ->
            when (response) {
                is BaseResult.Success -> {
                    BaseResult.Success(
                        response.data.map {
                            val sPrice = it.sPrice.dropLast(1).replace(",", "").toInt()
                            val nPrice = it.nPrice.dropLast(1).replace(",", "").toInt()
                            val percentage =
                                if (nPrice == 0) 0 else 100 - (sPrice.toDouble() / nPrice * 100).toInt()
                            UiDishItem(
                                hash = it.detailHash,
                                title = it.title,
                                isInserted = false,
                                image = it.image,
                                description = it.description,
                                sPrice = sPrice,
                                nPrice = nPrice,
                                salePercentage = "${percentage}%"
                            )
                        }.toList()
                    )
                }
                is BaseResult.Error -> {
                    BaseResult.Error(errorCode = response.errorCode)
                }
            }
        }
    }
}