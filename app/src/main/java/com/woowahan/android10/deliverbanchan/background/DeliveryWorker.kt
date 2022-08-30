package com.woowahan.android10.deliverbanchan.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class DeliveryWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val orderRepository: OrderRepository
) : CoroutineWorker(appContext, workerParams) {

    private val orderHashList = inputData.getStringArray("orderHashArray")!!.toList()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            orderRepository.updateOrderIsDelivering(orderHashList)
            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }
}