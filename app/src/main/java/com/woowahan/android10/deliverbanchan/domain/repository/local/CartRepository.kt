package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Cart
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    @WorkerThread
    fun getAllCartInfo(): Flow<List<CartInfo>>
    @WorkerThread
    fun getCartInfoById(hash: String): Flow<CartInfo>

    @WorkerThread
    suspend fun insertCartInfo(cartInfo: CartInfo)

    @WorkerThread
    suspend fun deleteCartInfo(hash: String)

    @WorkerThread
    suspend fun isExistCartInfo(hash: String): Boolean
    @WorkerThread
    fun getAllCartJoinList(): Flow<List<Cart>>
    @WorkerThread
    suspend fun updateCartChecked(hash: String, checked: Boolean)
    @WorkerThread
    suspend fun updateCartAmount(hash: String, amount: Int)
    @WorkerThread
    suspend fun insertCartInfoVarArg(vararg cartInfo: CartInfo)
    @WorkerThread
    suspend fun insertAndDeleteCartItems(cartInfo: List<CartInfo>, deleteHashes: List<String>)
    @WorkerThread
    suspend fun deleteCartInfoByHashList(deleteHashes: List<String>)
}