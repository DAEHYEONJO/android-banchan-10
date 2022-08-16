package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Cart
import com.woowahan.android10.deliverbanchan.data.local.model.join.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM CART_INFO")
    fun getAllCartInfo(): Flow<List<CartInfo>>

    @Query("SELECT * FROM CART_INFO WHERE hash = :hash")
    fun getCartInfoById(hash: String): Flow<CartInfo>

    @Query("SELECT * FROM LOCAL_DISH NATURAL JOIN CART_INFO")
    fun getAllCartJoinList(): Flow<List<Cart>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartInfo(cartInfo: CartInfo)

    @Query("DELETE FROM CART_INFO WHERE hash = :hash")
    suspend fun deleteCartInfo(hash: String)

    @Query("SELECT EXISTS(SELECT * FROM CART_INFO WHERE hash = :hash)")
    fun isExistCartInfo(hash: String): Boolean

}