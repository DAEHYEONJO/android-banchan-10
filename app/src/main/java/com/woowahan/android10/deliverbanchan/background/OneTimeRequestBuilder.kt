package com.woowahan.android10.deliverbanchan.background

import android.util.Log
import androidx.work.*

inline fun <reified T: ListenableWorker> getOneTimeRequestBuilder(data: Data): OneTimeWorkRequest {
    Log.e("WorkManager", "getOneTimeRequestBuilder: getOneTimeRequestBuilder 콜드", )
    return OneTimeWorkRequestBuilder<T>()
        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        .setInputData(data)
        .build()
}