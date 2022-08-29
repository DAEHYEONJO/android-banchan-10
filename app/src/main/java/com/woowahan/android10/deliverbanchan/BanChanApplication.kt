package com.woowahan.android10.deliverbanchan

import android.app.Application
import android.content.ComponentCallbacks2
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltAndroidApp
class BanChanApplication : Application(), Configuration.Provider, ComponentCallbacks2 {

    companion object {
        const val TAG = "GlobalApplication"
        lateinit var applicationScope: CoroutineScope
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

}