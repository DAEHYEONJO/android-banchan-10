package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.RecentViewed
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentViewedDao {

    @Query("SELECT * FROM RECENTLY_VIEWED_INFO ORDER BY time_stamp DESC")
    fun getAllRecentlyViewedInfo(): Flow<List<RecentViewedInfo>>

    @Query("SELECT * FROM LOCAL_DISH NATURAL JOIN RECENTLY_VIEWED_INFO ORDER BY time_stamp DESC LIMIT 7")
    fun getAllRecentlyJoinList(): Flow<List<RecentViewed>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentlyViewedInfo(recentViewedInfo: RecentViewedInfo)

    @Query("DELETE FROM RECENTLY_VIEWED_INFO")
    suspend fun deleteAllRecentlyViewedInfo()

}