package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.remote.model.Exhibition
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetExhibitionListUseCase @Inject constructor(
    private val dishItemRepository: DishItemRepository
) {
    suspend operator fun invoke(): Flow<BaseResult<List<Exhibition.CategoryItem>, Int>> {
        return dishItemRepository.getExhibitionDishes()
    }
}