package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.domain.common.convertPriceToInt
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapUiDishItemUseCase @Inject constructor() {
    suspend operator fun invoke(dishItem: DishItem, isInserted: Boolean): UiDishItem {
        Log.e("MapUiDishItemUseCase", "called")
        val nPriceInt = dishItem.nPrice.convertPriceToInt()
        val sPriceInt = dishItem.sPrice.convertPriceToInt()
        val percentage =
            if (nPriceInt == 0) 0 else 100 - (sPriceInt.toDouble() / nPriceInt * 100).toInt()

        return UiDishItem(
            hash = dishItem.detailHash,
            title = dishItem.title,
            isInserted = isInserted,
            image = dishItem.image,
            description = dishItem.description,
            sPrice = sPriceInt,
            nPrice = nPriceInt,
            salePercentage = percentage
        )
    }
}