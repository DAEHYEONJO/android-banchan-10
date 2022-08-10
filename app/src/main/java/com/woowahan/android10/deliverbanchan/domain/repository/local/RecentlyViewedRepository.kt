package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woowahan.android10.deliverbanchan.data.local.model.RecentlyViewedInfo
import kotlinx.coroutines.flow.Flow

interface RecentlyViewedRepository {
    fun getAllRecentlyViewedInfo(): Flow<List<RecentlyViewedInfo>>

    @WorkerThread
    suspend fun insertRecentlyViewedInfo(recentlyViewedInfo: RecentlyViewedInfo)

    @WorkerThread
    suspend fun deleteAllRecentlyViewedInfo()

}