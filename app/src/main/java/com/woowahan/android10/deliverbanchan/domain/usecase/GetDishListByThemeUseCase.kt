package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDishListByThemeUseCase @Inject constructor(
    private val dishItemRepository: DishItemRepository
) {
    suspend operator fun invoke(theme: String): Flow<BaseResult<List<DishItem>, Int>> {
        return dishItemRepository.getDishesByTheme(theme)
    }
}