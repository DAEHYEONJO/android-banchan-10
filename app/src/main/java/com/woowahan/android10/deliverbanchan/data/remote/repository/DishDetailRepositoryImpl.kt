package com.woowahan.android10.deliverbanchan.data.remote.repository

import com.woowahan.android10.deliverbanchan.data.remote.dao.DishApi
import com.woowahan.android10.deliverbanchan.data.remote.model.DishDetail
import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.data.remote.model.Exhibition
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishDetailRepositoryImpl @Inject constructor(
    private val dishApi: DishApi
): DishDetailRepository{

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