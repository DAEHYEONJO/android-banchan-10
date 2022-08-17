package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.dao.RecentlyViewedDao
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentlyViewedInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentlyViewed
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentlyViewedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentlyViewedRepositoryImpl @Inject constructor(
    private val recentlyViewedDao: RecentlyViewedDao
) : RecentlyViewedRepository {
    @WorkerThread
    override fun getAllRecentlyViewedInfo(): Flow<List<RecentlyViewedInfo>> =
        recentlyViewedDao.getAllRecentlyViewedInfo()

    @WorkerThread
    override suspend fun insertRecentlyViewedInfo(recentlyViewedInfo: RecentlyViewedInfo) =
        recentlyViewedDao.insertRecentlyViewedInfo(recentlyViewedInfo)

    @WorkerThread
    override suspend fun deleteAllRecentlyViewedInfo() =
        recentlyViewedDao.deleteAllRecentlyViewedInfo()
    @WorkerThread
    override fun getAllRecentlyJoinList(): Flow<List<RecentlyViewed>> = recentlyViewedDao.getAllRecentlyJoinList()

}