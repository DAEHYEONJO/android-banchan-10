package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentViewed
import kotlinx.coroutines.flow.Flow

interface RecentlyViewedRepository {
    @WorkerThread
    fun getAllRecentlyViewedInfo(): Flow<List<RecentViewedInfo>>
    @WorkerThread
    suspend fun insertRecentlyViewedInfo(recentViewedInfo: RecentViewedInfo)
    @WorkerThread
    suspend fun deleteAllRecentlyViewedInfo()
    @WorkerThread
    fun getAllRecentlyJoinList(): Flow<List<RecentViewed>>
}