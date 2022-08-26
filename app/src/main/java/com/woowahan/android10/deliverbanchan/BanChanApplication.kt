package com.woowahan.android10.deliverbanchan

import android.app.Application
import android.content.ComponentCallbacks2
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject

@HiltAndroidApp
class BanChanApplication : Application(), Configuration.Provider, ComponentCallbacks2 {

    companion object {
        const val TAG = "GlobalApplication"
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.e(TAG, "onLowMemory: ", )
    }

    override fun onTrimMemory(level: Int) {
        Log.e(TAG, "onTrimMemory: $level", )
        applicationScope.cancel()
        super.onTrimMemory(level)
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND,
            ComponentCallbacks2.TRIM_MEMORY_MODERATE,
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE -> {
                applicationScope.cancel()
            }
        }
    }
}