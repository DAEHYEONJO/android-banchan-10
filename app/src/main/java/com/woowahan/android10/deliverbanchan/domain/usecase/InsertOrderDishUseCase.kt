package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.OrderDish
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertOrderDishUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderDish: OrderDish) = orderRepository.insertOrderDish(orderDish)
}