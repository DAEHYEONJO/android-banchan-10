package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.UiCartJoinItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllOrderJoinListUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(): Flow<List<UiCartJoinItem>> {
        return orderRepository.getAllOrderJoinList().map {
           it.map{ order ->
               UiCartJoinItem(
                   order.hash,
                   order.title,
                   order.amount,
                   false,
                   order.description,
                   order.nPrices,
                   order.sPrice,
                   order.image,
                   order.amount*order.sPrice
               ).apply {
                   timeStamp = order.timeStamp
                   isDelivering = order.isDelivering
                   deliveryPrice = order.deliveryPrice
               }
           }
        }
    }
}