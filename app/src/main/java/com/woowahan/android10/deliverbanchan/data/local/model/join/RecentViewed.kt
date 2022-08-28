package com.woowahan.android10.deliverbanchan.data.local.model.join

import androidx.room.ColumnInfo

data class RecentViewed(
    val _id: Int,
    val hash: String,
    val title: String,
    val image: String,
    val description: String,
    val isInserted: Boolean,
    @ColumnInfo(name = "n_price") val nPrice: Int,
    @ColumnInfo(name = "s_price") val sPrice: Int,
    @ColumnInfo(name = "time_stamp") val timeStamp: Long
)
