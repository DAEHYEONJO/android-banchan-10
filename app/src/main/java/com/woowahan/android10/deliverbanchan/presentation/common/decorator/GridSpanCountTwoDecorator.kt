package com.woowahan.android10.deliverbanchan.presentation.common.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.presentation.common.ext.dpToPx
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class GridSpanCountTwoDecorator @Inject constructor(
    @ApplicationContext private val context: Context
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        val totalItemCount = state.itemCount
        if (position == 0 || position == 1) {
            outRect.top = dpToPx(context, 16).roundToInt()
        } else {
            outRect.top = dpToPx(context, 32).roundToInt()
        }
        if (position % 2 == 0) {
            outRect.left = dpToPx(context, 16).roundToInt()
            outRect.right = dpToPx(context, 4).roundToInt()
        } else {
            outRect.left = dpToPx(context, 4).roundToInt()
            outRect.right = dpToPx(context, 16).roundToInt()
        }

        if (totalItemCount % 2 == 0) {
            if (position == totalItemCount - 1 || position == totalItemCount - 2) {
                outRect.bottom = dpToPx(context, 40).roundToInt()
            }
        } else {
            if (position == totalItemCount - 1) {
                outRect.bottom = dpToPx(context, 40).roundToInt()
            }
        }
    }
}