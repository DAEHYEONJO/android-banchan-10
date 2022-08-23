package com.woowahan.android10.deliverbanchan.domain.model

data class UiCartOrderDishJoinItem(
    val hash: String,
    val title: String,
    var amount: Int,
    var checked: Boolean,
    val description: String,
    var nPrice: Int = 0,
    var sPrice: Int,
    val image: String,
    var totalPrice: Int = sPrice * amount,
    var timeStamp: Long = 0L,
    var isDelivering: Boolean = false,
    var deliveryPrice: Int = 0
) {

    companion object {
        fun emptyItem() = UiCartOrderDishJoinItem(
            hash = "",
            title = "",
            amount = 0,
            checked = false,
            description = "",
            nPrice = 0,
            sPrice = 0,
            image = "",
            totalPrice = 0
        )
    }
}
