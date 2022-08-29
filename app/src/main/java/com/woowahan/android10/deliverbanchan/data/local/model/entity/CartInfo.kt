package com.woowahan.android10.deliverbanchan.data.local.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "cart_info", indices = [Index("hash")])
data class CartInfo(
    @PrimaryKey
    val hash: String,
    val checked: Boolean = true,
    val amount: Int
) {
    companion object {
        fun emptyItem() = CartInfo("", true, -1)
    }
}
