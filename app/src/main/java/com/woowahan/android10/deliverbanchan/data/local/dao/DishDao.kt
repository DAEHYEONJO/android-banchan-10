package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.*
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {

    @Query("SELECT * FROM LOCAL_DISH")
    fun getAllLocalDish(): Flow<List<LocalDish>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocalDish(localDish: LocalDish)

}