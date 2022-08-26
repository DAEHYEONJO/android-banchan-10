package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem
import com.woowahan.android10.deliverbanchan.domain.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class GetUiExhibitionItemsUseCase @Inject constructor(
    private val dishItemRepository: DishItemRepository
) {
    suspend operator fun invoke(): Flow<BaseResult<List<UiExhibitionItem>>> {
        return dishItemRepository.getExhibitionDishes()
    }
}