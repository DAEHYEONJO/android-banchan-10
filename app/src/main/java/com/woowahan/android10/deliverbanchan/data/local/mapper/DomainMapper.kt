package com.woowahan.android10.deliverbanchan.data.local.mapper

import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.domain.model.TempOrder
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem

object DomainMapper {

    fun mapToLocalDish(uiDishItem: UiDishItem): LocalDish = with(uiDishItem) {
        LocalDish(
            hash,
            title,
            image,
            description,
            nPrice,
            sPrice
        )
    }

    fun mapToRecentViewedInfo(
        hash: String,
        timeStamp: Long,
        isInserted: Boolean
    ): RecentViewedInfo {
        return RecentViewedInfo(hash = hash, timeStamp = timeStamp, isInserted = isInserted)
    }

    fun mapToCartInfo(hash: String, checked: Boolean, amount: Int): CartInfo {
        return CartInfo(hash, checked, amount)
    }

    fun mapToCartInfo(uiCartOrderDishJoinItem: UiCartOrderDishJoinItem): CartInfo =
        with(uiCartOrderDishJoinItem) {
            return CartInfo(
                hash, checked, amount
            )
        }

    fun mapToOrderInfo(
        tempOrder: TempOrder,
        timeStamp: Long,
        isDelivering: Boolean,
        deliveryPrice: Int
    ) = with(tempOrder) {
        OrderInfo(
            hash, timeStamp, amount, isDelivering, deliveryPrice
        )
    }

}