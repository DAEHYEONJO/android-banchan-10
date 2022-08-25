package com.woowahan.android10.deliverbanchan.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_info", primaryKeys = ["hash", "time_stamp"])
data class OrderInfo(
    val hash: String,
    @ColumnInfo(name = "time_stamp")val timeStamp: Long,
    val amount: Int,
    @ColumnInfo(name = "is_delivering")val isDelivering: Boolean,
    @ColumnInfo(name = "delivery_price")val deliveryPrice: Int
)
