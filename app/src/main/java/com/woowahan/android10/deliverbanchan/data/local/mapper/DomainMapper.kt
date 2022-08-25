package com.woowahan.android10.deliverbanchan.data.local.mapper

import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem

object DomainMapper {

    fun mapToLocalDish(uiDishItem: UiDishItem): LocalDish = with(uiDishItem){
        LocalDish(
            hash,
            title,
            image,
            description,
            nPrice,
            sPrice
        )
    }

    fun mapToRecentViewedInfo(hash: String, timeStamp: Long): RecentViewedInfo{
        return RecentViewedInfo(hash = hash, timeStamp = timeStamp)
    }

    fun mapToCartInfo(hash: String, checked: Boolean, amount: Int): CartInfo{
        return CartInfo(hash, checked, amount)
    }
}