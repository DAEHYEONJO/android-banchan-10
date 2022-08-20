package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.dao.CartDao
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Cart
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
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

    override suspend fun insertAndDeleteAllItems(
        cartInfo: List<CartInfo>,
        deleteHashes: List<String>
    ) {
        cartDao.insertAndDeleteAllItems(cartInfo, deleteHashes)
    }

    override suspend fun deleteVarArgByHashList(deleteHashes: List<String>) {
        cartDao.deleteVarArgByHashList(deleteHashes)
    }


}
