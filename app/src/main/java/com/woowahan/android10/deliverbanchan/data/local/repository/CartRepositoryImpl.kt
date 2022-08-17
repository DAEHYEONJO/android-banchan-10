package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.annotation.WorkerThread
import androidx.room.Query
import com.woowahan.android10.deliverbanchan.data.local.dao.CartDao
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Cart
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CartRepository {

    override fun getAllCartInfo(): Flow<List<CartInfo>> = cartDao.getAllCartInfo()

    override fun getCartInfoById(hash: String): Flow<CartInfo> = cartDao.getCartInfoById(hash)

    @WorkerThread
    override suspend fun insertCartInfo(cartInfo: CartInfo) = cartDao.insertCartInfo(cartInfo)

    @WorkerThread
    override suspend fun deleteCartInfo(hash: String) = cartDao.deleteCartInfo(hash)

    @WorkerThread
    override suspend fun isExistCartInfo(hash: String): Boolean = cartDao.isExistCartInfo(hash)

    override fun getAllCartJoinList(): Flow<List<Cart>> = cartDao.getAllCartJoinList()
    override suspend fun updateCartChecked(hash: String, checked: Boolean) {
        withContext(dispatcher) {
            cartDao.updateCartChecked(hash, checked)
        }
    }

    override suspend fun updateCartAmount(hash: String, amount: Int) {
        withContext(dispatcher) {
            cartDao.updateCartAmount(hash, amount)
        }
    }


}
