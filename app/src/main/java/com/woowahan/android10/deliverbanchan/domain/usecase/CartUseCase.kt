package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.*
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartBottomBody
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartHeader
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

    fun getCartJoinMultiViewTypeList(): Flow<List<UiCartMultiViewType>> {
        return cartRepository.getAllCartJoinList().map { cartList ->
            val multiViewTypeList = mutableListOf<UiCartMultiViewType>().apply {
                add(UiCartMultiViewType(0, uiCartHeader = UiCartHeader.emptyItem()))
            }
            var deliveryPrice = 2500
            var productTotalPrice = 0
            var checkedCount = 0
            cartList.map { cart ->
                with(cart) {
                    val dishTotalPrice = sPrice * amount
                    productTotalPrice += dishTotalPrice
                    if (checked) checkedCount++
                    multiViewTypeList.add(
                        UiCartMultiViewType(
                            viewType = 1,
                            uiCartOrderDishJoinItem = UiCartOrderDishJoinItem(
                                hash = hash,
                                title = title,
                                amount = amount,
                                checked = checked,
                                nPrice = nPrice,
                                description = description,
                                sPrice = sPrice,
                                image = image,
                                totalPrice = dishTotalPrice
                            )
                        )
                    )
                }
            }
            var totalPrice = productTotalPrice + deliveryPrice
            val isAvailableDelivery = productTotalPrice >= UiCartBottomBody.MIN_DELIVERY_PRICE
            var isAvailableFreeDeliver = false
            if (productTotalPrice >= UiCartBottomBody.DELIVERY_FREE_PRICE) {
                totalPrice -= deliveryPrice
                deliveryPrice = 0
                if (isAvailableDelivery) isAvailableFreeDeliver = true
            }
            multiViewTypeList.add(
                UiCartMultiViewType(
                    viewType = 2,
                    uiCartBottomBody = UiCartBottomBody(
                        productTotalPrice = productTotalPrice,
                        deliveryPrice = deliveryPrice,
                        totalPrice = totalPrice,
                        isAvailableDelivery = isAvailableDelivery,
                        isAvailableFreeDelivery = isAvailableFreeDeliver
                    )
                )
            )
            if (checkedCount == cartList.size) {
                multiViewTypeList.first().uiCartHeader!!.apply {
                    checkBoxText = UiCartHeader.TEXT_SELECT_RELEASE
                    checkBoxChecked = true
                }
            } else {
                multiViewTypeList.first().uiCartHeader!!.apply {
                    checkBoxText = UiCartHeader.TEXT_SELECT_ALL
                    checkBoxChecked = false
                }
            }
            multiViewTypeList
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