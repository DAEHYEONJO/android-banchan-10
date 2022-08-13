package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.common.convertPriceToInt
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSideDishListUseCase @Inject constructor(
    private val dishItemRepository: DishItemRepository,
    private val isExistCartInfoUseCase: IsExistCartInfoUseCase
) {

    suspend operator fun invoke(): Flow<BaseResult<List<UiDishItem>, Int>> {
        return dishItemRepository.getSideDishes().map { response ->
            when (response) {
                is BaseResult.Success -> {
                    BaseResult.Success(
                        response.data.mapIndexed { index, dishItem ->
                            val sPrice = dishItem.sPrice.convertPriceToInt()
                            val nPrice = dishItem.nPrice.convertPriceToInt()
                            val percentage = if (nPrice == 0) 0 else 100 - (sPrice.toDouble() / nPrice * 100).toInt()
                            UiDishItem(
                                hash = dishItem.detailHash,
                                title = dishItem.title,
                                isInserted =  isExistCartInfoUseCase(dishItem.detailHash),
                                image = dishItem.image,
                                description = dishItem.description,
                                sPrice = sPrice,
                                nPrice = nPrice,
                                salePercentage = percentage,
                                index = index
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