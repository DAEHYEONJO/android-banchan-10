package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
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

    suspend fun getAllRecentJoinListLimitSeven(): Flow<List<UiDishItem>> {
        return recentRepository.getAllRecentJoinListLimitSeven().map { recentlyViewedList ->
            recentlyViewedList.map { recentlyViewed ->
                with(recentlyViewed) {
                    val nPrice = this.nPrice
                    val sPrice = this.sPrice
                    val percentage = if (nPrice == 0) 0 else 100 - (sPrice.toDouble() / nPrice * 100).toInt()
                    Log.e("getRecentlyJoinList", "getRecentlyJoinList: $nPrice $sPrice $percentage", )
                    val inInserted = cartRepository.isExistCartInfo(hash)
                    UiDishItem(
                        hash = hash,
                        title = title,
                        image = image,
                        nPrice = nPrice,
                        sPrice = sPrice,
                        salePercentage = percentage,
                        description = description,
                        timeStamp = timeStamp,
                        isInserted = inInserted
                    )
                }
            }
        }
    }
}