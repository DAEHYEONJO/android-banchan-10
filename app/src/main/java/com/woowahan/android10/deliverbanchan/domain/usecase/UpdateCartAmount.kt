package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class UpdateCartAmount @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend operator fun invoke(hash: String, amount: Int){
        cartRepository.updateCartAmount(hash, amount)
    }
}