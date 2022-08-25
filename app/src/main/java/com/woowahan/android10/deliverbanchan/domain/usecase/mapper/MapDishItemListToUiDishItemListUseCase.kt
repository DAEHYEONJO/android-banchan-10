package com.woowahan.android10.deliverbanchan.domain.usecase.mapper

import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.domain.common.convertPriceToInt
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.usecase.local.IsExistCartInfoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

// 정리 완료
@Singleton
class MapDishItemListToUiDishItemListUseCase @Inject constructor(
    private val isExistCartInfoUseCase: IsExistCartInfoUseCase,
) {
    suspend operator fun invoke(dishItemList: List<DishItem>): List<UiDishItem> {
        val uiDishItemList = MutableList(dishItemList.size) { UiDishItem.returnEmptyItem() }
        coroutineScope { // coroutineScope = 자체가 suspend 함수
            dishItemList.mapIndexed { index, dishItem ->
                async(Dispatchers.IO) {
                    val nPriceInt = dishItem.nPrice.convertPriceToInt()
                    val sPriceInt = dishItem.sPrice.convertPriceToInt()
                    val percentage =
                        if (nPriceInt == 0) 0 else 100 - (sPriceInt.toDouble() / nPriceInt * 100).toInt()

                    uiDishItemList[index] = UiDishItem(
                        hash = dishItem.detailHash,
                        title = dishItem.title,
                        isInserted = isExistCartInfoUseCase(dishItem.detailHash),
                        image = dishItem.image,
                        description = dishItem.description,
                        sPrice = sPriceInt,
                        nPrice = nPriceInt,
                        salePercentage = percentage,
                        index = index
                    )
                }
            }
        }
        return uiDishItemList
    }
}