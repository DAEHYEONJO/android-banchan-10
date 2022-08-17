package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsExistCartInfoUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend operator fun invoke(hash: String) = cartRepository.isExistCartInfo(hash)
}