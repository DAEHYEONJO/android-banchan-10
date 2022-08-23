package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllCartInfoUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(coroutineScope: CoroutineScope) = cartRepository.getAllCartInfo().shareIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
}