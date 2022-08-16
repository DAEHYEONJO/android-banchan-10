package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertOrderInfoUseCase @Inject constructor(
    private val orderRepository: OrderRepository
){
    suspend operator fun invoke(orderInfo: OrderInfo) = orderRepository.insertOrderInfo(orderInfo)
}