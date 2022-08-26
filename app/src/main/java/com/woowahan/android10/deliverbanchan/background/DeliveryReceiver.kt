package com.woowahan.android10.deliverbanchan.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.presentation.order.host.OrderActivity

class DeliveryReceiver : BroadcastReceiver() {

    companion object {
        val CHANNEL_ID = "Banchan"
        val NOTIFICATION_ID = 1000
    }

    override fun onReceive(context: Context, intent: Intent?) {

        val orderHashList = intent?.getStringArrayListExtra("orderHashList") ?: ArrayList<String>()
        val orderFirstItemTitle = intent?.getStringExtra("firstItemTitle") ?: "Title"
        val orderTimeStamp = intent?.getLongExtra("timeStamp", 0L) ?: 0L
        val deliveryWorkManager = WorkManager.getInstance(context)

        val deliveryWorkRequest = OneTimeWorkRequestBuilder<DeliveryWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("DeliveryWorker")
            .setInputData(getWorkerData(orderHashList))
            .build()

        deliveryWorkManager.enqueue(deliveryWorkRequest)

        createNotificationChannel(context)
        deliverNotification(
            context,
            getNotificationTitle(orderFirstItemTitle, orderHashList.size),
            orderTimeStamp ?: 0L
        )
    }

    private fun getWorkerData(orderHashList: List<String>): Data {
        val builder = Data.Builder().apply {
            putStringArray("orderHashArray", orderHashList.toTypedArray())
        }
        return builder.build()
    }

    private fun getNotificationTitle(firstItemTitle: String, listSize: Int): String {
        return if (listSize <= 1) firstItemTitle
        else "$firstItemTitle 외 ${listSize - 1}개"
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

    private fun deliverNotification(context: Context, contentTitle: String, orderTimeStamp: Long) {
        val contentIntent = Intent(context, OrderActivity::class.java).apply {
            // pending intent가 여러개 전달된 경우 각각의 extra 값을 구별해주기 위하여 설정함
            // action값이 다른 경우 다른 intent object로 판별됨
            action = System.currentTimeMillis().toString()
            putExtra("orderTimeStamp", orderTimeStamp)
            putExtra("fromReceiver", contentTitle)
        }

        val resultPendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(contentIntent)
            getPendingIntent(0, FLAG_MUTABLE or FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_banchan_foreground) // 아이콘
            .setContentTitle(contentTitle) // 제목
            .setContentText("배송이 완료되었습니다") // 내용
            .setContentIntent(resultPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context)
            .notify((System.currentTimeMillis() / 1000).toInt(), builder.build())
    }
}