package com.woowahan.android10.deliverbanchan.presentation.common.ext

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

inline fun <T, reified VH : RecyclerView.ViewHolder?> ListAdapter<T, VH>.observeItemRangeMoved(): Flow<Int> =
    callbackFlow {
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                trySend(fromPosition)
            }
        }
        registerAdapterDataObserver(
            observer
        )
        awaitClose {
            unregisterAdapterDataObserver(observer)
        }
    }
