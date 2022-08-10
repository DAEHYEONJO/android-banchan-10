package com.woowahan.android10.deliverbanchan.data.repository

import com.woowahan.android10.deliverbanchan.data.remote.model.*
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.data.remote.DishApi
import com.woowahan.android10.deliverbanchan.domain.repository.DishRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishRemoteRepositoryImpl @Inject constructor(
    private val dishApi: DishApi
) : DishRemoteRepository {

    companion object {
        const val TAG = "DishRemoteRepositoryImpl"
    }

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

    override suspend fun getDetailDish(hash: String): Flow<BaseResult<DishDetail.DishDetailData, Int>> =
        flow {
            val response = dishApi.getDetailDish(hash)
            with(response) {
                if (isSuccessful) {
                    val dishDetailData = body()!!.data
                    emit(BaseResult.Success(dishDetailData))
                } else {
                    emit(BaseResult.Error(code()))
                }
            }
        }

}