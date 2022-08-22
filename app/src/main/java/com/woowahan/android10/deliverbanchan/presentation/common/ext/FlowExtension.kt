package com.woowahan.android10.deliverbanchan.presentation.common.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.throttleFirst(duration: Long): Flow<T> = flow {
    var lastEmitTime = 0L
    collect { upstream ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmitTime > duration) {
            lastEmitTime = currentTime
            emit(upstream)
        }
    }
}

