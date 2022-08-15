package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllOrderJoinListUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke() = orderRepository.getAllOrderJoinList()
}