package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.UiCartJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiRecentlyJoinItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentlyViewedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetJoinUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val recentlyRepository: RecentlyViewedRepository
) {
    fun getOrderJoinList() = orderRepository.getAllOrderJoinList()

    fun getCartJoinList(): Flow<List<UiCartJoinItem>> {
        return cartRepository.getAllCartJoinList().map { cartList ->
            cartList.map { cart ->
                with(cart){
                    val totalPrice = sPrice*amount
                    UiCartJoinItem(
                        hash = hash,
                        title = title,
                        amount = amount,
                        checked = checked,
                        nPrice = nPrice,
                        sPrice = sPrice,
                        image = image,
                        totalPrice = totalPrice
                    )
                }
            }
        }
    }

    fun getRecentlyJoinList(): Flow<List<UiRecentlyJoinItem>> {
        return recentlyRepository.getAllRecentlyJoinList().map { recentlyViewedList ->
            recentlyViewedList.map { recentlyViewed ->
                with(recentlyViewed) {
                    val inInserted = cartRepository.isExistCartInfo(hash)
                    UiRecentlyJoinItem(
                        hash = hash,
                        title = title,
                        image = image,
                        nPrice = nPrice,
                        sPrice = sPrice,
                        timeStamp = timeStamp,
                        isInserted = inInserted
                    )
                }
            }
        }
    }
}