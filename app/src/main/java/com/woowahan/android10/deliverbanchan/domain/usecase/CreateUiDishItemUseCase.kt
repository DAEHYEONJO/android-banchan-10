package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateUiDishItemUseCase @Inject constructor() {
    suspend operator fun invoke(dishItem: DishItem): UiDishItem {
        var sPrice = dishItem.sPrice
        sPrice = sPrice.replace(",", "")
        sPrice = sPrice.removeRange(sPrice.length - 1, sPrice.length)

        var nPrice = dishItem.nPrice
        nPrice = nPrice.replace(",", "")
        nPrice = nPrice.removeRange(nPrice.length - 1, nPrice.length)

        return UiDishItem(
            dishItem.detailHash,
            dishItem.title,
            false,
            dishItem.image,
            dishItem.description,
            sPrice.toInt(),
            nPrice.toInt()
        )
    }
}