package com.woowahan.android10.deliverbanchan.data.local.model

import androidx.room.ColumnInfo


data class Order(
    val hash: String,
    val title: String,
    val image: String,
    @ColumnInfo(name = "s_price")val sPrice: String,
    @ColumnInfo(name = "time_stamp")val timeStamp: Long,
    val amount: Int,
    @ColumnInfo(name = "is_delivering")val isDelivering: Boolean
)
