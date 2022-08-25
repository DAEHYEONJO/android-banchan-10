package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.TempOrder
import com.woowahan.android10.deliverbanchan.domain.model.UiBottomSheet
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityRetainedScoped
class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val recentRepository: RecentViewedRepository,
    private val orderRepository: OrderRepository
) {
    suspend fun deleteCartInfoByHashList(hashList: List<String>) {
        cartRepository.deleteCartInfoByHashList(hashList)
    }

    fun getBottomSheetInfoByHash(hash: String): Flow<UiBottomSheet> {
        return cartRepository.getBottomSheetCartInfoByHash(hash)
    }

    fun getCartJoinList(): Flow<List<UiCartOrderDishJoinItem>> {
        return cartRepository.getAllCartJoinList().map { cartList ->
            cartList.map { cart ->
                with(cart) {
                    val totalPrice = sPrice * amount
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

    fun getAllRecentJoinListLimitSeven(): Flow<List<UiDishItem>> {
        return recentRepository.getAllRecentJoinListLimitSeven().map { recentlyViewedList ->
            recentlyViewedList.map { recentlyViewed ->
                with(recentlyViewed) {
                    val nPrice = this.nPrice
                    val sPrice = this.sPrice
                    val percentage =
                        if (nPrice == 0) 0 else 100 - (sPrice.toDouble() / nPrice * 100).toInt()
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

    suspend fun insertAndDeleteCartItems(
        uiCartOrderDishJoinList: List<UiCartOrderDishJoinItem>,
        deleteHashes: List<String>
    ) {
        cartRepository.insertAndDeleteCartItems(uiCartOrderDishJoinList, deleteHashes)
    }

    suspend fun insertVarArgOrderInfo(
        tempOrderSet: Set<TempOrder>,
        timeStamp: Long,
        isDelivering: Boolean,
        deliveryPrice: Int
    ) {
        orderRepository.insertVarArgOrderInfo(tempOrderSet, timeStamp, isDelivering, deliveryPrice)
    }
}