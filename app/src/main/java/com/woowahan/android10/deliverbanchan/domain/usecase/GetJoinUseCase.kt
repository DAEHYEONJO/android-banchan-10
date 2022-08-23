package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetJoinUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val recentRepository: RecentViewedRepository
) {
    fun getOrderJoinList() = orderRepository.getAllOrderJoinList()

    fun getCartJoinList(): Flow<List<UiCartOrderDishJoinItem>> {
        return cartRepository.getAllCartJoinList().map { cartList ->
            cartList.map { cart ->
                with(cart){
                    val totalPrice = sPrice*amount
                    UiCartOrderDishJoinItem(
                        hash = hash,
                        title = title,
                        amount = amount,
                        checked = checked,
                        nPrice = nPrice,
                        description = description,
                        sPrice = sPrice,
                        image = image,
                        totalPrice = totalPrice
                    )
                }
            }
        }
    }

    suspend fun getRecentlyJoinList(): Flow<List<UiDishItem>> {
        return recentRepository.getAllRecentJoinList().map { recentlyViewedList ->
            recentlyViewedList.map { recentlyViewed ->
                with(recentlyViewed) {
                    val inInserted = cartRepository.isExistCartInfo(hash)
                    UiDishItem(
                        hash = hash,
                        title = title,
                        image = image,
                        nPrice = nPrice,
                        sPrice = sPrice,
                        description = description,
                        timeStamp = timeStamp,
                        isInserted = inInserted
                    )
                }
            }
        }
    }
}