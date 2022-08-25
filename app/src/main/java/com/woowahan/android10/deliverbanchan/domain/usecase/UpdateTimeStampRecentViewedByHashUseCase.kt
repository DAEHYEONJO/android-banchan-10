package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class UpdateTimeStampRecentViewedByHashUseCase @Inject constructor(
    private val recentViewedRepository: RecentViewedRepository
){
    suspend operator fun invoke(hash: String, timeStamp: Long){
        recentViewedRepository.updateTimeStampRecentViewedByHash(hash, timeStamp)
    }
}