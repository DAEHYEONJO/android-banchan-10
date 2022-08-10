package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woowahan.android10.deliverbanchan.data.local.model.Order
import com.woowahan.android10.deliverbanchan.data.local.model.OrderDish
import com.woowahan.android10.deliverbanchan.data.local.model.OrderInfo
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    fun getAllOrderDish(): Flow<List<OrderDish>>

    fun getAllOrderInfo(): Flow<List<OrderInfo>>

    @WorkerThread
    suspend fun insertOrderDish(orderDish: OrderDish)

    @WorkerThread
    suspend fun insertOrderInfo(orderInfo: OrderInfo)

    @WorkerThread
    suspend fun deleteOrderInfo(hash: String)

    @WorkerThread
    suspend fun deleteOrderDish(hash: String)

    fun getAllOrderJoinList(): Flow<List<Order>>
}