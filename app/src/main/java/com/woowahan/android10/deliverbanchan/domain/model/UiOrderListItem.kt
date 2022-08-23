package com.woowahan.android10.deliverbanchan.domain.model


data class UiOrderListItem(
    val timeStamp: Long,
    val orderList: List<UiCartJoinItem>
)