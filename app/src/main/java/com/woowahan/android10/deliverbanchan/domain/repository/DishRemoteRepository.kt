package com.woowahan.android10.deliverbanchan.domain.repository

import com.woowahan.android10.deliverbanchan.data.remote.model.*
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import kotlinx.coroutines.flow.Flow

interface DishRemoteRepository {

    suspend fun getSideDishes(): Flow<BaseResult<List<DishItem>, Int>>

    suspend fun getSoupDishes(): Flow<BaseResult<List<DishItem>, Int>>

    suspend fun getMainDishes(): Flow<BaseResult<List<DishItem>, Int>>

    suspend fun getExhibitionDishes(): Flow<BaseResult<List<Exhibition.CategoryItem>, Int>>

    suspend fun getDetailDish(
        hash: String
    ): Flow<BaseResult<DishDetail.DishDetailData, Int>>

}