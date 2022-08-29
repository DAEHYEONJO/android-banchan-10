package com.woowahan.android10.deliverbanchan.data.remote.repository

import com.woowahan.android10.deliverbanchan.data.remote.dao.DishApi
import com.woowahan.android10.deliverbanchan.data.remote.mapper.ApiMapper
import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishDetailRepositoryImpl @Inject constructor(
    private val dishApi: DishApi
) : DishDetailRepository {

    override suspend fun getDetailDish(
        hash: String,
        uiDishItem: UiDishItem
    ): Flow<BaseResult<UiDetailInfo>> = flow {
        kotlin.runCatching {
            dishApi.getDetailDish(hash)
        }.onFailure { throwable ->
            emit(BaseResult.Error(throwable.message.toString()))
        }.onSuccess { response ->
            if (response.isSuccessful) {
                val dishDetailData = response.body()!!.data
                emit(
                    BaseResult.Success(
                        ApiMapper.mapToUiDetailInfo(
                            dishDetailData, uiDishItem
                        )
                    )
                )
            } else {
                emit(BaseResult.Error(response.code().toString()))
            }
        }
    }
}