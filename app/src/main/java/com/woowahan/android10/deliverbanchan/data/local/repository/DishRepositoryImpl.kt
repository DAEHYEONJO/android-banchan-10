package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.dao.DishDao
import com.woowahan.android10.deliverbanchan.data.local.mapper.DomainMapper
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.DishRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DishRepositoryImpl @Inject constructor(
    private val dishDao: DishDao
) : DishRepository {
    @WorkerThread
    override suspend fun insertLocalDish(uiDishItem: UiDishItem) {
        dishDao.insertLocalDish(
            DomainMapper.mapToLocalDish(
                uiDishItem
            )
        )
    }

    override fun getAllLocalDish(): Flow<List<LocalDish>> {
        return dishDao.getAllLocalDish()
    }

}