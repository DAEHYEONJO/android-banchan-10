package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.*
import com.woowahan.android10.deliverbanchan.data.local.model.join.Order
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM LOCAL_DISH")
    fun getAllOrderDish(): Flow<List<LocalDish>>

    @Query("SELECT * FROM order_info")
    fun getAllOrderInfo(): Flow<List<OrderInfo>>

    @Query("SELECT * FROM LOCAL_DISH NATURAL JOIN ORDER_INFO ORDER BY TIME_STAMP DESC")
    fun getAllOrderJoinList(): Flow<List<Order>>

    @Insert
    suspend fun insertOrderInfo(orderInfo: OrderInfo)

    @Query("DELETE FROM ORDER_INFO WHERE hash = :hash")
    suspend fun deleteOrderInfo(hash: String)

    @Query("DELETE FROM LOCAL_DISH WHERE hash = :hash")
    suspend fun deleteOrderDish(hash: String)

    @Insert
    suspend fun insertVarArgOrderInfo(vararg orderInfo: OrderInfo)

    @Query("UPDATE ORDER_INFO SET is_delivering = :isDelivering WHERE hash = :hash")
    suspend fun updateOrderInfo(hash: String, isDelivering: Boolean)

    @Transaction
    suspend fun updateOrderInfoByHashList(orderHashList: List<String>) {
        orderHashList.forEach { hash ->
            updateOrderInfo(hash, false)
        }
    }
}