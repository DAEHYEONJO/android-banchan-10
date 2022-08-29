package com.woowahan.android10.deliverbanchan.presentation.cart.model


data class UiCartCompleteHeader(
    var isDelivering: Boolean,
    val orderTimeStamp: Long,
    val orderItemCount: Int
) {
    companion object {
        const val ESTIMATED_DELIVERY_TIME = 30 * 1000
        fun emptyItem() = UiCartCompleteHeader(
            false, 0L, 0
        )
    }
}
