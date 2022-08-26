package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentViewed
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentViewedDao {

    @Query("SELECT * FROM RECENT_VIEWED_INFO ORDER BY time_stamp DESC")
    fun getAllRecentViewedInfo(): Flow<List<RecentViewedInfo>>

    @Query("SELECT * FROM LOCAL_DISH NATURAL JOIN RECENT_VIEWED_INFO ORDER BY time_stamp DESC LIMIT 7")
    fun getAllRecentJoinListLimitSeven(): Flow<List<RecentViewed>>

    @Query("SELECT * FROM LOCAL_DISH NATURAL JOIN RECENT_VIEWED_INFO ORDER BY time_stamp DESC")
    fun getAllRecentJoinPaging(): PagingSource<Int, RecentViewed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentViewedInfo(recentViewedInfo: RecentViewedInfo)

    @Query("DELETE FROM RECENT_VIEWED_INFO")
    suspend fun deleteAllRecentViewedInfo()

    @Query("UPDATE RECENT_VIEWED_INFO SET time_stamp = :timeStamp WHERE hash = :hash")
    suspend fun updateTimeStampRecentViewedByHash(hash: String, timeStamp: Long)

}