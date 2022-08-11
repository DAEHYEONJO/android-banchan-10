package com.woowahan.android10.deliverbanchan.data.remote.repository

import android.util.Log
import com.woowahan.android10.deliverbanchan.data.remote.dao.DishApi
import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.data.remote.model.Exhibition
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DishItemRepositoryImpl @Inject constructor(
    private val dishApi: DishApi
): DishItemRepository {

    override suspend fun getSideDishes(): Flow<BaseResult<List<DishItem>, Int>> = flow {
        val response = dishApi.getSideDishes()
        with(response) {
            if (isSuccessful) {
                val dishList = body()!!.body
                emit(BaseResult.Success(dishList))
            } else {
                emit(BaseResult.Error(code()))
            }
        }
    }

    override suspend fun getSoupDishes(): Flow<BaseResult<List<DishItem>, Int>> = flow {
        val response = dishApi.getSoupDishes()
        with(response) {
            if (isSuccessful) {
                val dishList = body()!!.body
                emit(BaseResult.Success(dishList))
            } else {
                emit(BaseResult.Error(code()))
            }
        }
    }

    override suspend fun getMainDishes(): Flow<BaseResult<List<DishItem>, Int>> = flow {
        val response = dishApi.getMainDishes()
        with(response) {
            if (isSuccessful) {
                Log.e("AppTest", "maindish success")
                val dishList = body()!!.body
                emit(BaseResult.Success(dishList))
            } else {
                emit(BaseResult.Error(code()))
            }
        }
    }

    override suspend fun getExhibitionDishes(): Flow<BaseResult<List<Exhibition.CategoryItem>, Int>> =
        flow {
            val response = dishApi.getExhibitionDishes()
            with(response) {
                if (isSuccessful) {
                    val categoryItem = body()!!.body
                    emit(BaseResult.Success(categoryItem))
                } else {
                    emit(BaseResult.Error(code()))
                }
            }
        }
}