package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class DeleteCartInfoByHashUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(hash: String){
        cartRepository.deleteCartInfo(hash)
    }
}