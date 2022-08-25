package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.*
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

    @Update
    suspend fun updateCarts(vararg cartInfo: CartInfo)

    @Query("UPDATE CART_INFO SET checked = :checked WHERE hash = :hash")
    suspend fun updateCartChecked(hash: String, checked: Boolean)
    @Query("UPDATE CART_INFO SET amount = amount+:amount WHERE hash = :hash")
    suspend fun updateCartAmount(hash: String, amount: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartInfoVarArg(vararg cartInfo: CartInfo)

    @Transaction
    suspend fun deleteCartInfoByHashList(deleteHashes: List<String>){
        deleteHashes.forEach { hash ->
            deleteCartInfo(hash)
        }
    }

    @Transaction
    suspend fun insertAndDeleteCartItems(cartInfo: List<CartInfo>, deleteHashes: List<String>){
        insertCartInfoVarArg(*cartInfo.toTypedArray())
        deleteHashes.forEach {
            deleteCartInfo(it)
        }
    }

}