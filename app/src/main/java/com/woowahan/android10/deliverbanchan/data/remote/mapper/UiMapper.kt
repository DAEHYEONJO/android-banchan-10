package com.woowahan.android10.deliverbanchan.data.remote.mapper

import com.woowahan.android10.deliverbanchan.data.remote.model.DishDetail
import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.domain.common.convertPriceToInt
import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import javax.inject.Inject
import javax.inject.Singleton


object UiMapper {
    fun mapToUiDishItem(dishItem: DishItem, isInserted: Boolean, index: Int): UiDishItem {
        val sPrice = dishItem.sPrice.convertPriceToInt()
        val nPrice = dishItem.nPrice.convertPriceToInt()
        val percentage = if (nPrice == 0) 0 else 100 - (sPrice.toDouble() / nPrice * 100).toInt()
        return UiDishItem(
            hash = dishItem.detailHash,
            title = dishItem.title,
            isInserted = isInserted,
            image = dishItem.image,
            description = dishItem.description,
            sPrice = sPrice,
            nPrice = nPrice,
            salePercentage = percentage,
            index = index,
            timeStamp = 0L
        )
    }

    fun mapToUiDetailInfo(dishDetail: DishDetail.DishDetailData, uiDishItem: UiDishItem): UiDetailInfo {
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