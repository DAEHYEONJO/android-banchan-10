package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import kotlinx.coroutines.flow.Flow

interface DishRepository {

    @WorkerThread
    suspend fun insertLocalDish(uiDishItem: UiDishItem)
    @WorkerThread
    fun getAllLocalDish(): Flow<List<LocalDish>>

}