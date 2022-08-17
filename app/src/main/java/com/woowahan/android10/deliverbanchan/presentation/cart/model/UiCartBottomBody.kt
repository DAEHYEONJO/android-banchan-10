package com.woowahan.android10.deliverbanchan.presentation.cart.model

data class UiCartBottomBody(
    var productTotalPrice: Int,
    val deliveryPrice: Int = 2500,
    var totalPrice: Int
){
    companion object{
        fun emptyItem(): UiCartBottomBody = UiCartBottomBody(
            productTotalPrice = 0,
            deliveryPrice = 2500,
            totalPrice = 2500
        )
    }
}
