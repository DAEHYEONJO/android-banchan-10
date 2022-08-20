package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import androidx.paging.PagingSource
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentViewed
import kotlinx.coroutines.flow.Flow

interface RecentViewedRepository {
    @WorkerThread
    fun getAllRecentViewedInfo(): Flow<List<RecentViewedInfo>>
    @WorkerThread
    suspend fun insertRecentViewedInfo(recentViewedInfo: RecentViewedInfo)
    @WorkerThread
    suspend fun deleteAllRecentViewedInfo()
    @WorkerThread
    fun getAllRecentJoinList(): Flow<List<RecentViewed>>
    @WorkerThread
    fun getAllRecentJoinPaging(): PagingSource<Int, RecentViewed>
}