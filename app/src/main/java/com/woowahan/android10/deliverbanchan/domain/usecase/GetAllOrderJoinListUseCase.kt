package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityRetainedScoped
class GetAllOrderJoinListUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(): Flow<List<UiCartOrderDishJoinItem>> {
        return orderRepository.getAllOrderJoinList().map {
            it.map { order ->
                UiCartOrderDishJoinItem(
                    order.hash,
                    order.title,
                    order.amount,
                    false,
                    order.description,
                    order.nPrices,
                    order.sPrice,
                    order.image,
                    order.amount * order.sPrice,
                    timeStamp = order.timeStamp,
                    isDelivering = order.isDelivering,
                    deliveryPrice = order.deliveryPrice
                )
            }
        }
    }
}