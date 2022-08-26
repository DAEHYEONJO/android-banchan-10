package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.dao.OrderDao
import com.woowahan.android10.deliverbanchan.data.local.mapper.DomainMapper
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Order
import com.woowahan.android10.deliverbanchan.domain.model.TempOrder
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {

    override fun getAllOrderDish(): Flow<List<LocalDish>> = orderDao.getAllOrderDish()

    override fun getAllOrderInfo(): Flow<List<OrderInfo>> = orderDao.getAllOrderInfo()

    @WorkerThread
    override suspend fun insertOrderInfo(orderInfo: OrderInfo) = orderDao.insertOrderInfo(orderInfo)

    @WorkerThread
    override suspend fun deleteOrderInfo(hash: String) = orderDao.deleteOrderInfo(hash)

    @WorkerThread
    override suspend fun deleteOrderDish(hash: String) = orderDao.deleteOrderDish(hash)

    override fun getAllOrderJoinList(): Flow<List<Order>> = orderDao.getAllOrderJoinList()

    @WorkerThread
    override suspend fun insertVarArgOrderInfo(
        tempOrderSet: Set<TempOrder>,
        timeStamp: Long,
        isDelivering: Boolean,
        deliveryPrice: Int
    ) {

        orderDao.insertVarArgOrderInfo(
            *tempOrderSet.map { tempOrder ->
                DomainMapper.mapToOrderInfo(
                    tempOrder, timeStamp, isDelivering, deliveryPrice
                )
            }.toTypedArray()
        )
    }

    @WorkerThread
    override suspend fun updateOrderIsDelivering(orderHashList: List<String>) {
        orderDao.updateOrderInfoByHashList(orderHashList)
    }
}