package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.*
import com.woowahan.android10.deliverbanchan.data.local.model.Order
import com.woowahan.android10.deliverbanchan.data.local.model.OrderDish
import com.woowahan.android10.deliverbanchan.data.local.model.OrderInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
interface OrderDao {

    @Query("SELECT * FROM ORDER_DISH")
    fun getAllOrderDish(): Flow<List<OrderDish>>

    @Query("SELECT * FROM order_info")
    fun getAllOrderInfo(): Flow<List<OrderInfo>>

    @Query("SELECT * FROM ORDER_DISH NATURAL JOIN ORDER_INFO")
    fun getAllOrderJoinList(): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderDish(orderDish: OrderDish)

    @Insert
    suspend fun insertOrderInfo(orderInfo: OrderInfo)

    @Query("DELETE FROM ORDER_INFO WHERE hash = :hash")
    suspend fun deleteOrderInfo(hash: String)

    @Query("DELETE FROM ORDER_DISH WHERE hash = :hash")
    suspend fun deleteOrderDish(hash: String)

}