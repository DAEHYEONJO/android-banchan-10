package com.woowahan.android10.deliverbanchan.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recently_viewed_info")
data class RecentViewedInfo(
    @PrimaryKey
    val hash: String,
    @ColumnInfo(name = "time_stamp")val timeStamp: Long
)
