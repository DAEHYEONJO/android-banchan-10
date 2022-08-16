package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import kotlinx.coroutines.flow.Flow

interface DishRepository {

    @WorkerThread
    suspend fun insertLocalDish(localDish: LocalDish)

    fun getAllLocalDish(): Flow<List<LocalDish>>

}