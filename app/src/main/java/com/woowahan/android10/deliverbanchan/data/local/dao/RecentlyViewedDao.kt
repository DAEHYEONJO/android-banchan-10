package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woowahan.android10.deliverbanchan.data.local.model.RecentlyViewedInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyViewedDao {

    @Query("SELECT * FROM RECENTLY_VIEWED_INFO ORDER BY time_stamp ASC")
    fun getAllRecentlyViewedInfo(): Flow<List<RecentlyViewedInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentlyViewedInfo(recentlyViewedInfo: RecentlyViewedInfo)

    @Query("DELETE FROM RECENTLY_VIEWED_INFO")
    suspend fun deleteAllRecentlyViewedInfo()

}