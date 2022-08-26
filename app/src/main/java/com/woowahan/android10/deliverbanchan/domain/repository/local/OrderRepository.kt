package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Order
import com.woowahan.android10.deliverbanchan.domain.model.TempOrder
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    @WorkerThread
    fun getAllOrderDish(): Flow<List<LocalDish>>

    @WorkerThread
    fun getAllOrderInfo(): Flow<List<OrderInfo>>

    @WorkerThread
    suspend fun insertOrderInfo(orderInfo: OrderInfo)

    @WorkerThread
    suspend fun deleteOrderInfo(hash: String)

    @WorkerThread
    suspend fun deleteOrderDish(hash: String)

    @WorkerThread
    fun getAllOrderJoinList(): Flow<List<Order>>

    @WorkerThread
    suspend fun insertVarArgOrderInfo(
        tempOrderSet: Set<TempOrder>,
        timeStamp: Long,
        isDelivering: Boolean,
        deliveryPrice: Int
    )

    @WorkerThread
    suspend fun updateOrderIsDelivering(orderHashList: List<String>)
}