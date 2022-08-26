package com.woowahan.android10.deliverbanchan.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "local_dish", indices = [Index(value = ["hash"])])
data class LocalDish(
    @PrimaryKey
    val hash: String,
    val title: String,
    val image: String,
    val description: String,
    @ColumnInfo(name = "n_price") val nPrice: Int,
    @ColumnInfo(name = "s_price") val sPrice: Int
)
