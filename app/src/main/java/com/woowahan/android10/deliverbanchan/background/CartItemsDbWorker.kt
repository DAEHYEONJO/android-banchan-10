package com.woowahan.android10.deliverbanchan.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.woowahan.android10.deliverbanchan.data.local.dao.CartDao
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

@HiltWorker
class CartItemsDbWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cartRepository: CartRepository
) : CoroutineWorker(appContext, workerParams) {

    private val cartListStr = inputData.getString("cartListStr")
    private val deleteListStr = inputData.getStringArray("deleteListStr")!!.toList()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<CartInfo?>?>() {}.type
            val cartList: List<CartInfo>  = gson.fromJson(cartListStr, type)

            Log.e("LocalDBWorker", "$cartList")
            Log.e("LocalDbWorker", "$deleteListStr")

            cartRepository.insertAndDeleteCartItems(cartList, deleteListStr)

            Result.success()
        } catch (exception: Exception) {
            Log.e("LocalDBWorker", "${exception.javaClass}, ${exception.message}")
            Result.failure()
        }

    }
}