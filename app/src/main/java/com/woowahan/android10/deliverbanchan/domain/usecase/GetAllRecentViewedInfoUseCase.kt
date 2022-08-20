package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentViewedInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllRecentViewedInfoUseCase @Inject constructor(
    private val recentViewedRepository: RecentViewedRepository
) {
    operator fun invoke(): Flow<List<RecentViewedInfo>> {
         return recentViewedRepository.getAllRecentViewedInfo()
     }
}