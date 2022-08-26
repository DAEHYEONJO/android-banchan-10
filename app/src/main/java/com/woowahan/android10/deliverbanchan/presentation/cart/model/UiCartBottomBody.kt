package com.woowahan.android10.deliverbanchan.presentation.cart.model

data class UiCartBottomBody(
    var productTotalPrice: Int,
    val deliveryPrice: Int = 2500,
    var totalPrice: Int,
    var isAvailableDelivery: Boolean = false,
    var isAvailableFreeDelivery: Boolean = false,
    val minDeliveryPrice: Int = MIN_DELIVERY_PRICE,
    val deliveryFreePrice: Int = DELIVERY_FREE_PRICE
) {

    companion object {
        const val MIN_DELIVERY_PRICE: Int = 10000
        const val DELIVERY_FREE_PRICE: Int = 40000
        fun emptyItem(): UiCartBottomBody = UiCartBottomBody(
            productTotalPrice = 0,
            deliveryPrice = 2500,
            totalPrice = 2500
        )
    }

}
