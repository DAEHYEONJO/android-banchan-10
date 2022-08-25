package com.woowahan.android10.deliverbanchan.data.remote.mapper

import com.woowahan.android10.deliverbanchan.data.remote.model.DishDetail
import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.data.common.ext.convertPriceToInt
import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem


object ApiMapper {
    fun mapToUiDishItem(dishItem: DishItem, isInserted: Boolean, index: Int): UiDishItem =
        with(dishItem) {
            val sPrice = sPrice.convertPriceToInt()
            val nPrice = nPrice.convertPriceToInt()
            val percentage =
                if (nPrice == 0) 0 else 100 - (sPrice.toDouble() / nPrice * 100).toInt()
            return UiDishItem(
                hash = detailHash,
                title = title,
                isInserted = isInserted,
                image = image,
                description = description,
                sPrice = sPrice,
                nPrice = nPrice,
                salePercentage = percentage,
                index = index,
                timeStamp = 0L
            )
        }

    fun mapToUiDetailInfo(
        dishDetail: DishDetail.DishDetailData,
        uiDishItem: UiDishItem
    ): UiDetailInfo {
        return UiDetailInfo(
            hash = uiDishItem.hash,
            title = uiDishItem.title,
            isInserted = uiDishItem.isInserted,
            image = uiDishItem.image,
            description = uiDishItem.description,
            point = dishDetail.point,
            deliveryInfo = dishDetail.deliveryInfo,
            deliveryFee = dishDetail.deliveryFee,
            thumbList = dishDetail.thumbImages,
            detailSection = dishDetail.detailSection,
            sPrice = uiDishItem.sPrice,
            nPrice = uiDishItem.nPrice,
            salePercentage = uiDishItem.salePercentage,
        )
    }
}