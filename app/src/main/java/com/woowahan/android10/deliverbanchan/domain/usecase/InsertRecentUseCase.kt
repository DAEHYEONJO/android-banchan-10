package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.DishRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

// 정리 완료
@ActivityRetainedScoped
class InsertRecentUseCase @Inject constructor(
    private val recentViewedRepository: RecentViewedRepository,
    private val dishRepository: DishRepository
) {
    suspend operator fun invoke(localDish: LocalDish, recentViewedInfo: RecentViewedInfo){
        recentViewedRepository.insertRecentViewedInfo(recentViewedInfo)
        dishRepository.insertLocalDish(localDish)
    }
}