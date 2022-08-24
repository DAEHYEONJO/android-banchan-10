package com.woowahan.android10.deliverbanchan.presentation.cart.model


data class UiCartCompleteHeader(
    val orderTimeStamp: Long,
    val orderItemCount: Int
){
    companion object{
        const val ESTIMATED_DELIVERY_TIME = 10 * 1000
        fun emptyItem() = UiCartCompleteHeader(
            0L, 0
        )
    }
}
