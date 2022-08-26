package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.dao.CartDao
import com.woowahan.android10.deliverbanchan.data.local.mapper.DomainMapper
import com.woowahan.android10.deliverbanchan.data.local.mapper.EntityMapper
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Cart
import com.woowahan.android10.deliverbanchan.domain.model.UiBottomSheet
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun getAllCartInfo(): Flow<List<CartInfo>> = cartDao.getAllCartInfo()

    override fun getBottomSheetCartInfoByHash(hash: String): Flow<UiBottomSheet> {
        return cartDao.getCartInfoById(hash).map { cartInfo ->
            EntityMapper.mapToUiBottomSheet(cartInfo)
        }
    }

    @WorkerThread
    override suspend fun insertCartInfo(hash: String, checked: Boolean, amount: Int) {
        cartDao.insertCartInfo(
            DomainMapper.mapToCartInfo(
                hash, checked, amount
            )
        )
    }

    @WorkerThread
    override suspend fun deleteCartInfo(hash: String) = cartDao.deleteCartInfo(hash)

    @WorkerThread
    override suspend fun isExistCartInfo(hash: String): Boolean = cartDao.isExistCartInfo(hash)

    override fun getAllCartJoinList(): Flow<List<Cart>> = cartDao.getAllCartJoinList()

    @WorkerThread
    override suspend fun updateCartChecked(hash: String, checked: Boolean) {
        cartDao.updateCartChecked(hash, checked)
    }

    @WorkerThread
    override suspend fun updateCartAmount(hash: String, amount: Int) {
        cartDao.updateCartAmount(hash, amount)
    }

    override suspend fun insertCartInfoVarArg(vararg cartInfo: CartInfo) {
        cartDao.insertCartInfoVarArg(*cartInfo)
    }

    override suspend fun insertAndDeleteCartItems(
        uiCartOrderDishJoinList: List<UiCartOrderDishJoinItem>,
        deleteHashes: List<String>
    ) {
        cartDao.insertAndDeleteCartItems(
            uiCartOrderDishJoinList.map { DomainMapper.mapToCartInfo(it) },
            deleteHashes
        )
    }

    override suspend fun deleteCartInfoByHashList(deleteHashes: List<String>) {
        cartDao.deleteCartInfoByHashList(deleteHashes)
    }


}
