package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.DishRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class InsertLocalDishAndRecentUseCase @Inject constructor(
    private val recentViewedRepository: RecentViewedRepository,
    private val dishRepository: DishRepository
) {
    suspend operator fun invoke(
        uiDishItem: UiDishItem,
        hash: String,
        timeStamp: Long,
        isInserted: Boolean
    ) {
        recentViewedRepository.insertRecentViewedInfo(hash, timeStamp, isInserted)
        dishRepository.insertLocalDish(uiDishItem)
    }
}