package com.woowahan.android10.deliverbanchan.data.remote.repository

import com.woowahan.android10.deliverbanchan.data.remote.dao.DishApi
import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.data.remote.model.Exhibition
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishItemRepositoryImpl @Inject constructor(
    private val dishApi: DishApi
): DishItemRepository {

    companion object{
        const val TAG = "DishItemRepositoryImpl"
    }

    override suspend fun getDishesByTheme(theme: String): Flow<BaseResult<List<DishItem>, Int>> = flow {
        val response = dishApi.getDishesByTheme(theme)
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
}