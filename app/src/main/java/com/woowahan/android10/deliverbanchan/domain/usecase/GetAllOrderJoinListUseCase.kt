package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllOrderInfoListUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke() = orderRepository.getAllOrderInfo()
}