package com.woowahan.android10.deliverbanchan.domain.model

data class UiOrderInfo(
    val itemPrice: Int,
    val deliveryFee: Int,
    val totalPrice: Int
) {
    companion object {
        fun emptyItem() = UiOrderInfo(
            0, 0, 0
        )
    }
}