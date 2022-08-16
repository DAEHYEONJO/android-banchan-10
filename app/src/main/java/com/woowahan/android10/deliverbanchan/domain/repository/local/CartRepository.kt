package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Cart
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getAllCartInfo(): Flow<List<CartInfo>>

    fun getCartInfoById(hash: String): Flow<CartInfo>

    @WorkerThread
    suspend fun insertCartInfo(cartInfo: CartInfo)

    @WorkerThread
    suspend fun deleteCartInfo(hash: String)

    @WorkerThread
    fun isExistCartInfo(hash: String): Boolean

    fun getAllCartJoinList(): Flow<List<Cart>>
}