package com.woowahan.android10.deliverbanchan.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
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
import java.security.SecureRandom
import kotlin.random.Random

class DeliveryReceiver : BroadcastReceiver() {

    companion object {
        val CHANNEL_ID = "Banchan"
        val NOTIFICATION_ID = 1000
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.e("DeliveryReceiver", "onReceive")

        val orderHashList =
            intent?.let { it.getStringArrayListExtra("orderHashList") } ?: ArrayList<String>()
        Log.e("DeliveryReceiver", "orderHashList : $orderHashList")

        val orderFirstItemTitle = intent?.let { it.getStringExtra("firstItemTitle") } ?: "Title"

        val deliveryWorkManager = WorkManager.getInstance(context)

        val deliveryWorkRequest = OneTimeWorkRequestBuilder<DeliveryWorker>()
            //.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("DeliveryWorker")
            .setInputData(getWorkerData(orderHashList))
            .build()

        deliveryWorkManager.enqueue(deliveryWorkRequest)

        createNotificationChannel(context)
        deliverNotification(context, getNotificationTitle(orderFirstItemTitle, orderHashList.size))
    }

    private fun getWorkerData(orderHashList: List<String>): Data {
        val builder = Data.Builder().apply {
            putStringArray("orderHashArray", orderHashList.toTypedArray())
        }
        return builder.build()
    }

    private fun getNotificationTitle(firstItemTitle: String, listSize: Int): String {
        if (listSize <= 1) return firstItemTitle
        else return "$firstItemTitle 외 ${listSize - 1}개"
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

    private fun deliverNotification(context: Context, contentTitle: String) {
        val contentIntent = Intent(context, OrderActivity::class.java).apply {
            //flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val resultPendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(contentIntent)
            getPendingIntent(0, PendingIntent.FLAG_MUTABLE)
        }

//        val contentPendingIntent = PendingIntent.getActivity(
//            context,
//            NOTIFICATION_ID, // requestCode
//            contentIntent, // 알림 클릭 시 이동할 인텐트
//            PendingIntent.FLAG_MUTABLE
//        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_cart) // 아이콘
            .setContentTitle(contentTitle) // 제목
            .setContentText("배송이 완료되었습니다") // 내용
            .setContentIntent(resultPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        Log.e("AppTest", "alarm notify")
        NotificationManagerCompat.from(context).notify((System.currentTimeMillis()/1000).toInt(), builder.build())
    }
}