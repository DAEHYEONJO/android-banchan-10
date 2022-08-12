package com.woowahan.android10.deliverbanchan.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "order_dish", indices = [Index(value = ["hash"])])
data class OrderDish(
    @PrimaryKey
    val hash: String,
    val title: String,
    val image: String,
    @ColumnInfo(name = "s_price")val sPrice: Int
)
