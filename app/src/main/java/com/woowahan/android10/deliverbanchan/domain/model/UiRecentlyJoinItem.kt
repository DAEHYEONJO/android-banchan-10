package com.woowahan.android10.deliverbanchan.domain.model

data class UiRecentlyJoinItem(
    val hash: String,
    val title: String,
    val image: String,
    val nPrice: Int,
    val sPrice: Int,
    val timeStamp: Long,
    val isInserted: Boolean
) {
    companion object{
        fun emptyItem() = UiRecentlyJoinItem(
             hash = "",
             title = "",
             image = "",
             nPrice = 0,
             sPrice = 0,
             timeStamp = 0L,
             isInserted = false
        )
    }
}
