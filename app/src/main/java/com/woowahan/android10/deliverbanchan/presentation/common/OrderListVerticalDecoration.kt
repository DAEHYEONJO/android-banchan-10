package com.woowahan.android10.deliverbanchan.presentation.common

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.presentation.common.ext.dpToPx

class OrderListVerticalDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val total = state.itemCount

        val offset = dpToPx(context, 16).toInt()
        outRect.top = offset
        if (position == total - 1) outRect.bottom = offset
    }
}