package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.common.convertPriceToInt
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetThemeDishListUseCase @Inject constructor(
    private val dishItemRepository: DishItemRepository,
    private val isExistCartInfoUseCase: IsExistCartInfoUseCase
) {

    companion object{
        const val TAG = "GetThemeDishListUseCase"
    }

    suspend operator fun invoke(theme: String): Flow<BaseResult<List<UiDishItem>, Int>> {
        return dishItemRepository.getDishesByTheme(theme).map { response ->
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
                    Log.e(TAG, "invoke: ${response.errorCode}", )
                    BaseResult.Error(errorCode = response.errorCode)
                }
            }
        }
    }
}