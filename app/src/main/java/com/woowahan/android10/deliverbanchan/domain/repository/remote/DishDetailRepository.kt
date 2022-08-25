package com.woowahan.android10.deliverbanchan.domain.repository.remote

import com.woowahan.android10.deliverbanchan.domain.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import kotlinx.coroutines.flow.Flow

interface DishDetailRepository {
    suspend fun getDetailDish(
        hash: String,
        uiDishItem: UiDishItem
    ): Flow<BaseResult<UiDetailInfo>>
}