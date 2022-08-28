package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentViewed
import kotlinx.coroutines.flow.Flow

interface RecentViewedRepository {
    @WorkerThread
    fun getAllRecentViewedInfo(): Flow<List<RecentViewedInfo>>

    @WorkerThread
    suspend fun insertRecentViewedInfo(hash: String, timeStamp: Long, isInserted: Boolean)

    @WorkerThread
    suspend fun deleteAllRecentViewedInfo()

    @WorkerThread
    fun getAllRecentJoinListLimitSeven(): Flow<List<RecentViewed>>

    @WorkerThread
    fun getAllRecentJoinPager(): Pager<Int, RecentViewed>

    @WorkerThread
    suspend fun updateTimeStampRecentViewedByHash(hash: String, timeStamp: Long)

    @WorkerThread
    suspend fun updateRecentIsInsertedInCart(hash: String, isInserted: Boolean)

    @WorkerThread
    suspend fun updateVarArgRecentIsInsertedFalseInCart(hashList: List<String>)
}