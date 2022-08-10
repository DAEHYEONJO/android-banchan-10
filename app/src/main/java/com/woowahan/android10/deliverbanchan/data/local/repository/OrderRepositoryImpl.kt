package com.woowahan.android10.deliverbanchan.data.local.repository

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.dao.OrderDao
import com.woowahan.android10.deliverbanchan.data.local.model.Order
import com.woowahan.android10.deliverbanchan.data.local.model.OrderDish
import com.woowahan.android10.deliverbanchan.data.local.model.OrderInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
): OrderRepository {
    override fun getAllOrderDish(): Flow<List<OrderDish>> = orderDao.getAllOrderDish()

    override fun getAllOrderInfo(): Flow<List<OrderInfo>> = orderDao.getAllOrderInfo()

    @WorkerThread
    override suspend fun insertOrderDish(orderDish: OrderDish) = orderDao.insertOrderDish(orderDish)

    @WorkerThread
    override suspend fun insertOrderInfo(orderInfo: OrderInfo) = orderDao.insertOrderInfo(orderInfo)

    @WorkerThread
    override suspend fun deleteOrderInfo(hash: String) = orderDao.deleteOrderInfo(hash)

    @WorkerThread
    override suspend fun deleteOrderDish(hash: String) = orderDao.deleteOrderDish(hash)

    override fun getAllOrderJoinList(): Flow<List<Order>> = orderDao.getAllOrderJoinList()
}