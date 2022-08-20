package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingSource
import com.woowahan.android10.deliverbanchan.data.local.dao.RecentViewedDao
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentViewed
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentViewedRepositoryImpl @Inject constructor(
    private val recentlyViewedDao: RecentViewedDao
) : RecentViewedRepository {

    override fun getAllRecentViewedInfo(): Flow<List<RecentViewedInfo>> =
        recentlyViewedDao.getAllRecentlyViewedInfo()

    @WorkerThread
    override suspend fun insertRecentViewedInfo(recentViewedInfo: RecentViewedInfo) =
        recentlyViewedDao.insertRecentViewedInfo(recentViewedInfo)

    @WorkerThread
    override suspend fun deleteAllRecentViewedInfo() =
        recentlyViewedDao.deleteAllRecentlyViewedInfo()

    override fun getAllRecentJoinList(): Flow<List<RecentViewed>> =
        recentlyViewedDao.getAllRecentlyJoinList()

    override fun getAllRecentJoinPaging(): PagingSource<Int, RecentViewed> =
        recentlyViewedDao.getAllRecentlyJoinPaging()

}