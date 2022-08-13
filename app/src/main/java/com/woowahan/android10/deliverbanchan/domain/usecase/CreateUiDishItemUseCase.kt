package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateUiDishItemUseCase @Inject constructor() {
    suspend operator fun invoke(dishItem: DishItem): UiDishItem {
        Log.e("CreateUiDishItemUseCase", "called")
        var sPrice = dishItem.sPrice
        sPrice = sPrice.replace(",", "")
        sPrice = sPrice.removeRange(sPrice.length - 1, sPrice.length)

        var nPrice = dishItem.nPrice
        nPrice = nPrice.replace(",", "")
        nPrice = nPrice.removeRange(nPrice.length - 1, nPrice.length)

        val nPriceInt = nPrice.toInt()
        val sPriceInt = sPrice.toInt()

        val percentage =
            if (nPriceInt == 0) 0 else 100 - (sPriceInt.toDouble() / nPriceInt * 100).toInt()

        return UiDishItem(
            hash = dishItem.detailHash,
            title = dishItem.title,
            isInserted = false,
            image = dishItem.image,
            description = dishItem.description,
            sPrice = sPriceInt,
            nPrice = nPriceInt,
            salePercentage = percentage
        )
    }
}