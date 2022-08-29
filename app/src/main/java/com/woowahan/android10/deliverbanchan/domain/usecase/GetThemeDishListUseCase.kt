package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class GetThemeDishListUseCase @Inject constructor(
    private val dishItemRepository: DishItemRepository
) {

    companion object {
        const val TAG = "GetThemeDishListUseCase"
    }

    suspend operator fun invoke(theme: String): Flow<BaseResult<List<UiDishItem>>> {
        return dishItemRepository.getDishesByTheme(theme)
    }
}