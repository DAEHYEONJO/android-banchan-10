package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateEmptyUiDetailInfoUseCase @Inject constructor() {
    operator fun invoke() = UiDetailInfo(
        hash = "",
        title = "",
        isInserted = false,
        image = "",
        description = "",
        point = "",
        deliveryInfo = "",
        deliveryFee = "",
        sPrice = 0,
        nPrice = 0,
        salePercentage = 0
    )
}