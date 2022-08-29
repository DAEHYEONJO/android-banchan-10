package com.woowahan.android10.deliverbanchan.presentation.common.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.presentation.common.ext.dpToPx
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import kotlin.math.roundToInt


@FragmentScoped
class GridSpanCountTwoForMainDishDecorator @Inject constructor(
    @ApplicationContext private val context: Context
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val totalItemCount = parent.adapter!!.itemCount

        if (position % 2 == 0) {
            outRect.left = dpToPx(context, 16).roundToInt()
            outRect.right = dpToPx(context, 4).roundToInt()
        } else {
            outRect.left = dpToPx(context, 4).roundToInt()
            outRect.right = dpToPx(context, 16).roundToInt()
        }
        outRect.bottom = dpToPx(context, 32).roundToInt()
    }
}