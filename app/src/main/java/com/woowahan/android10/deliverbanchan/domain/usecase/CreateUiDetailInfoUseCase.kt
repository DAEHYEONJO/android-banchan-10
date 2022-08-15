package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.remote.model.DishDetail
import com.woowahan.android10.deliverbanchan.domain.common.convertPriceToInt
import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateUiDetailInfoUseCase @Inject constructor() {
    operator fun invoke(uiDishItem: UiDishItem, dishDetailData: DishDetail.DishDetailData) =
        UiDetailInfo(
            hash = uiDishItem.hash,
            title = uiDishItem.title,
            isInserted = uiDishItem.isInserted,
            image = uiDishItem.image,
            description = uiDishItem.description,
            point = dishDetailData.point,
            deliveryInfo = dishDetailData.deliveryInfo,
            deliveryFee = dishDetailData.deliveryFee,
            sPrice = uiDishItem.sPrice,
            nPrice = uiDishItem.nPrice,
            salePercentage = uiDishItem.salePercentage
        )
}