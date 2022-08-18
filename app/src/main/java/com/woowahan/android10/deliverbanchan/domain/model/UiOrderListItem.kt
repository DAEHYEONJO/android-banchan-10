package com.woowahan.android10.deliverbanchan.domain.model

import com.woowahan.android10.deliverbanchan.data.local.model.join.Order

data class UiOrderListItem(
    val timeStamp: Long,
    val orderList: List<Order>
)