package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecentUseCase @Inject constructor(
    private val recentViewedRepository: RecentViewedRepository
){
    suspend fun updateRecentIsInsertedTrueInCartUseCase(hash: String){
        recentViewedRepository.updateRecentIsInsertedInCart(hash, true)
    }
    suspend fun updateVarArgRecentIsInsertedFalseInCartUseCase(hashList: List<String>){
        recentViewedRepository.updateVarArgRecentIsInsertedFalseInCart(hashList)
    }
}