package com.woowahan.android10.deliverbanchan.domain.repository.remote

import com.woowahan.android10.deliverbanchan.domain.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem
import kotlinx.coroutines.flow.Flow

interface DishItemRepository {
    suspend fun getDishesByTheme(theme: String): Flow<BaseResult<List<UiDishItem>>>

    suspend fun getExhibitionDishes(): Flow<BaseResult<List<UiExhibitionItem>>>
}