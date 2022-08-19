package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentlyViewedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllRecentlyViewedInfoUseCase @Inject constructor(
    private val recentlyViewedRepository: RecentlyViewedRepository
) {
    operator fun invoke(): Flow<List<RecentViewedInfo>> {
         return recentlyViewedRepository.getAllRecentlyViewedInfo()
     }
}