package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.woowahan.android10.deliverbanchan.data.local.dao.RecentViewedDao
import com.woowahan.android10.deliverbanchan.data.local.mapper.DomainMapper
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentViewed
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentViewedRepositoryImpl @Inject constructor(
    private val recentlyViewedDao: RecentViewedDao
) : RecentViewedRepository {

    override fun getAllRecentViewedInfo(): Flow<List<RecentViewedInfo>> =
        recentlyViewedDao.getAllRecentViewedInfo()

    override suspend fun insertRecentViewedInfo(
        hash: String,
        timeStamp: Long,
        isInserted: Boolean
    ) {
        recentlyViewedDao.insertRecentViewedInfo(
            DomainMapper.mapToRecentViewedInfo(
                hash, timeStamp, isInserted
            )
        )
    }

    @WorkerThread
    override suspend fun deleteAllRecentViewedInfo() =
        recentlyViewedDao.deleteAllRecentViewedInfo()

    override fun getAllRecentJoinListLimitSeven(): Flow<List<RecentViewed>> =
        recentlyViewedDao.getAllRecentJoinListLimitSeven()

    override fun getAllRecentJoinPager(): Pager<Int, RecentViewed> {
        return Pager(
            PagingConfig(
                pageSize = 8
            )
        ) {
            recentlyViewedDao.getAllRecentJoinPaging()
        }
    }

    override suspend fun updateTimeStampRecentViewedByHash(hash: String, timeStamp: Long) {
        recentlyViewedDao.updateTimeStampRecentViewedByHash(hash, timeStamp)
    }

    override suspend fun updateRecentIsInsertedInCart(hash: String, isInserted: Boolean) {
        recentlyViewedDao.updateRecentIsInsertedInCart(hash, isInserted)
    }

    override suspend fun updateVarArgRecentIsInsertedFalseInCart(hashList: List<String>) {
        recentlyViewedDao.updateVarArgRecentIsInsertedFalseInCart(hashList)
    }


}