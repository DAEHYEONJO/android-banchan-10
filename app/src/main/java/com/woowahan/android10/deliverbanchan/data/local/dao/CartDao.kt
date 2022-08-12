package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woowahan.android10.deliverbanchan.data.local.model.CartInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM CART_INFO")
    fun getAllCartInfo(): Flow<List<CartInfo>>

    @Query("SELECT * FROM CART_INFO WHERE hash = :hash")
    fun getCartInfoById(hash: String): Flow<CartInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartInfo(cartInfo: CartInfo)

    @Query("DELETE FROM CART_INFO WHERE hash = :hash")
    suspend fun deleteCartInfo(hash: String)

    @Query("SELECT EXISTS(SELECT * FROM CART_INFO WHERE hash = :hash)")
    fun isExistCartInfo(hash: String): Flow<Boolean>

}