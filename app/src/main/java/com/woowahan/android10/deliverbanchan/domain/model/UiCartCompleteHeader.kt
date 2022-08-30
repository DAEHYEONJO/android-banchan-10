package com.woowahan.android10.deliverbanchan.domain.model


data class UiCartCompleteHeader(
    var isDelivering: Boolean,
    val orderTimeStamp: Long,
    val orderItemCount: Int
) {
    companion object {
        const val ESTIMATED_DELIVERY_TIME = 20 * 1000
        fun emptyItem() = UiCartCompleteHeader(
            false, 0L, 0
        )
    }
}
