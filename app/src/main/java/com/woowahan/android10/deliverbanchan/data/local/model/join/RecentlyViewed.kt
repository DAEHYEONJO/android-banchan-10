package com.woowahan.android10.deliverbanchan.data.local.model.join

import androidx.room.ColumnInfo

data class RecentlyViewed(
    val hash: String,
    val title: String,
    val image: String,
    @ColumnInfo(name = "n_price")val nPrice: Int,
    @ColumnInfo(name = "s_price")val sPrice: Int,
    @ColumnInfo(name = "time_stamp")val timeStamp: Long
)
