package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCartInfoUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(hash: String): Flow<CartInfo> {
        return cartRepository.getCartInfoById(hash)
    }
}