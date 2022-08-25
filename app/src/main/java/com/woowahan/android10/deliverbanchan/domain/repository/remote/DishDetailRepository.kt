package com.woowahan.android10.deliverbanchan.domain.repository.remote

import com.woowahan.android10.deliverbanchan.data.remote.model.DishDetail
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import kotlinx.coroutines.flow.Flow

interface DishDetailRepository {
    suspend fun getDetailDish(
        hash: String
    ): Flow<BaseResult<DishDetail.DishDetailData, Int>>
}