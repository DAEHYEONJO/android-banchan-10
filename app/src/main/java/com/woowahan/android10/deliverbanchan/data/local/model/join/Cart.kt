package com.woowahan.android10.deliverbanchan.data.local.model.join

import androidx.room.ColumnInfo

data class Cart(
    val hash: String,
    val checked: Boolean,
    val amount: Int,
    val title: String,
    val image: String,
    val description: String,
    @ColumnInfo(name = "n_price") val nPrice: Int,
    @ColumnInfo(name = "s_price") val sPrice: Int
)