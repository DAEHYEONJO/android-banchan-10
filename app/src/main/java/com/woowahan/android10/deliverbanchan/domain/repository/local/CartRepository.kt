package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woowahan.android10.deliverbanchan.data.local.model.CartInfo
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getAllCartInfo(): Flow<List<CartInfo>>

    fun getCartInfoById(hash: String): Flow<CartInfo>

    @WorkerThread
    suspend fun insertCartInfo(cartInfo: CartInfo)

    @WorkerThread
    suspend fun deleteCartInfo(hash: String)

    @WorkerThread
    fun isExistCartInfo(hash: String): Flow<Boolean>
}