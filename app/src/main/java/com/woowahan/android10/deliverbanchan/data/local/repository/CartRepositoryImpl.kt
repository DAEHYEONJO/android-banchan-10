package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.room.Query
import com.woowahan.android10.deliverbanchan.data.local.dao.CartDao
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Cart
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
): CartRepository {
    override fun getAllCartInfo(): Flow<List<CartInfo>> = cartDao.getAllCartInfo()

    override fun getCartInfoById(hash: String): Flow<CartInfo> = cartDao.getCartInfoById(hash)

    override suspend fun insertCartInfo(cartInfo: CartInfo) = cartDao.insertCartInfo(cartInfo)

    override suspend fun deleteCartInfo(hash: String) = cartDao.deleteCartInfo(hash)

    override fun isExistCartInfo(hash: String): Boolean = cartDao.isExistCartInfo(hash)

    override fun getAllCartJoinList(): Flow<List<Cart>> = cartDao.getAllCartJoinList()

}
