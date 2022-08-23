package com.woowahan.android10.deliverbanchan.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.presentation.order.OrderActivity
import kotlin.random.Random

class DeliveryReceiver : BroadcastReceiver() {

    companion object {
        val CHANNEL_ID = "Banchan"
        val NOTIFICATION_ID = 1000
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.e("DeliveryReceiver", "onReceive")

        val orderHashList = intent?.let { it.getStringArrayListExtra("orderHashList") } ?: ArrayList<String>()
        Log.e("DeliveryReceiver", "orderHashList : $orderHashList")

        val deliveryWorkManager = WorkManager.getInstance(context)

        val deliveryWorkRequest = OneTimeWorkRequestBuilder<DeliveryWorker>()
            //.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("DeliveryWorker")
            .setInputData(getWorkerData(orderHashList))
            .build()

        deliveryWorkManager.enqueue(deliveryWorkRequest)

        createNotificationChannel(context)
        deliverNotification(context)
    }

    private fun getWorkerData(orderHashList: List<String>): Data {
        val builder = Data.Builder().apply {
            putStringArray("orderHashArray", orderHashList.toTypedArray())
        }
        return builder.build()
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Banchan Channel"
            val descriptionText = "This is Banchan Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, OrderActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID, // requestCode
            contentIntent, // 알림 클릭 시 이동할 인텐트
            PendingIntent.FLAG_MUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 아이콘
            .setContentTitle("배송완료") // 제목
            .setContentText("Test Content") // 내용
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        Log.e("AppTest", "alarm notify")
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}