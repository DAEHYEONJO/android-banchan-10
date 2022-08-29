package com.woowahan.android10.deliverbanchan.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "recent_viewed_info", indices = [Index("hash", unique = true)])
data class RecentViewedInfo(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val hash: String="",
    val isInserted: Boolean,
    @ColumnInfo(name = "time_stamp") val timeStamp: Long=0L
)
