package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentlyViewedInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentlyViewed
import kotlinx.coroutines.flow.Flow

interface RecentlyViewedRepository {
    @WorkerThread
    fun getAllRecentlyViewedInfo(): Flow<List<RecentlyViewedInfo>>
    @WorkerThread
    suspend fun insertRecentlyViewedInfo(recentlyViewedInfo: RecentlyViewedInfo)
    @WorkerThread
    suspend fun deleteAllRecentlyViewedInfo()
    @WorkerThread
    fun getAllRecentlyJoinList(): Flow<List<RecentlyViewed>>
}