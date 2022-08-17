package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentlyViewedInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.DishRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentlyViewedRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InsertRecentlyUseCase @Inject constructor(
    private val recentlyViewedRepository: RecentlyViewedRepository,
    private val dishRepository: DishRepository
) {
    suspend operator fun invoke(localDish: LocalDish, recentlyViewedInfo: RecentlyViewedInfo){
        recentlyViewedRepository.insertRecentlyViewedInfo(recentlyViewedInfo)
        dishRepository.insertLocalDish(localDish)
    }
}