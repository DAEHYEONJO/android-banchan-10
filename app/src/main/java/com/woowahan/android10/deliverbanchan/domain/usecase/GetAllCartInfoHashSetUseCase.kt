package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllCartInfoHashSetUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(): Flow<Set<String>> {
        return cartRepository.getAllCartInfo().map { cartInfoList ->
            cartInfoList.map { it.hash }.toSet()
        }
    }
}