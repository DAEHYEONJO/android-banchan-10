package com.woowahan.android10.deliverbanchan.presentation.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class OrderListVerticalDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val total = parent.layoutManager!!.itemCount

        val offset = dpToPx(view.context, 10).toInt()
        outRect.top = offset

        if (position == total - 1) outRect.bottom = offset
    }
}