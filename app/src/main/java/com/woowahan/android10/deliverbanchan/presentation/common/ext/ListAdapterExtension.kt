package com.woowahan.android10.deliverbanchan.presentation.common.ext

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

inline fun <reified VH : RecyclerView.ViewHolder?> RecyclerView.Adapter<VH>.observeItemRangeMoved(): Flow<Unit> =
    callbackFlow {
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                trySend(Unit)
            }
        }
        registerAdapterDataObserver(observer)
        awaitClose {
            unregisterAdapterDataObserver(observer)
        }
    }
