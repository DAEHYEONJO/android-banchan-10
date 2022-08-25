package com.woowahan.android10.deliverbanchan.domain.model


data class UiOrderListItem(
    val timeStamp: Long,
    val curDeliveryTotalPrice: Int,
    val orderList: List<UiCartOrderDishJoinItem>
)