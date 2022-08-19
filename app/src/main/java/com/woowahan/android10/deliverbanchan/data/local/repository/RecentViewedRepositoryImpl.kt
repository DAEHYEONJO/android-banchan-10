package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.dao.RecentViewedDao
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentViewed
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentlyViewedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentViewedRepositoryImpl @Inject constructor(
    private val recentlyViewedDao: RecentViewedDao
) : RecentlyViewedRepository {

    override fun getAllRecentlyViewedInfo(): Flow<List<RecentViewedInfo>> =
        recentlyViewedDao.getAllRecentlyViewedInfo()

    @WorkerThread
    override suspend fun insertRecentlyViewedInfo(recentViewedInfo: RecentViewedInfo) =
        recentlyViewedDao.insertRecentlyViewedInfo(recentViewedInfo)

    @WorkerThread
    override suspend fun deleteAllRecentlyViewedInfo() =
        recentlyViewedDao.deleteAllRecentlyViewedInfo()

    override fun getAllRecentlyJoinList(): Flow<List<RecentViewed>> =
        recentlyViewedDao.getAllRecentlyJoinList()

}