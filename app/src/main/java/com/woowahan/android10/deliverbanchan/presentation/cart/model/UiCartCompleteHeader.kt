package com.woowahan.android10.deliverbanchan.presentation.cart.model


data class UiCartCompleteHeader(
    val orderTimeStamp: Long,
    val orderItemCount: Int
){
    companion object{
        fun emptyItem() = UiCartCompleteHeader(
            0L, 0
        )
    }
}
