package com.woowahan.android10.deliverbanchan.presentation.common.ext

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun View.toGone(){
    visibility = View.GONE
}

fun View.toVisible(){
    visibility = View.VISIBLE
}

fun View.toInvisible(){
    visibility = View.INVISIBLE
}

fun View.onClickCallBackFlow(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        trySend(Unit).isSuccess
    }
    awaitClose {
        setOnClickListener(null)
    }
}

inline fun View.setClickEventWithDuration(
    coroutineScope: CoroutineScope,
    duration: Long = 2000,
    crossinline onClick: () -> Unit,
) {
    onClickCallBackFlow()
        .throttleFirst(duration)
        .onEach { onClick() }
        .launchIn(coroutineScope)
}