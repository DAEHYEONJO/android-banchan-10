package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllOrderInfoListUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(coroutineScope: CoroutineScope) = orderRepository.getAllOrderInfo().shareIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
}