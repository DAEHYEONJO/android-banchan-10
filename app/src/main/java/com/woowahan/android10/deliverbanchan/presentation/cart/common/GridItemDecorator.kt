package com.woowahan.android10.deliverbanchan.presentation.cart.common

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.presentation.common.ext.dpToPx
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlin.math.roundToInt

@ActivityRetainedScoped
class GridItemDecorator(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        val totalItemCount = state.itemCount
        val scrollPosition = state.targetScrollPosition
        if (position == 0) {
            outRect.left = dpToPx(context, 16).roundToInt()
        } else {
            outRect.left = dpToPx(context, 8).roundToInt()
        }
        if (position == totalItemCount - 1) {
            outRect.right = dpToPx(context, 16).roundToInt()
        }
    }
}