package com.woowahan.android10.deliverbanchan.domain.usecase.local

import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import javax.inject.Inject
import javax.inject.Singleton

// 정리 완료
@Singleton
class IsExistCartInfoUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend operator fun invoke(hash: String) = cartRepository.isExistCartInfo(hash)
}