package com.woowahan.android10.deliverbanchan.domain.repository.remote

import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.data.remote.model.Exhibition
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import kotlinx.coroutines.flow.Flow

interface DishItemRepository {
    suspend fun getSideDishes(): Flow<BaseResult<List<DishItem>, Int>>

    suspend fun getSoupDishes(): Flow<BaseResult<List<DishItem>, Int>>

    suspend fun getMainDishes(): Flow<BaseResult<List<DishItem>, Int>>

    suspend fun getExhibitionDishes(): Flow<BaseResult<List<Exhibition.CategoryItem>, Int>>
}