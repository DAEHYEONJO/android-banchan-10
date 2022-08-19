package com.woowahan.android10.deliverbanchan.data.local.model.join

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Order(
    val hash: String,
    val title: String,
    val image: String,
    @ColumnInfo(name = "n_price")val nPrices: Int,
    @ColumnInfo(name = "s_price")val sPrice: Int,
    @ColumnInfo(name = "time_stamp")val timeStamp: Long,
    val amount: Int,
    @ColumnInfo(name = "is_delivering")val isDelivering: Boolean
)
