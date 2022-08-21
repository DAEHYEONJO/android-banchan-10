package com.woowahan.android10.deliverbanchan.presentation.common.decorator

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.presentation.common.ext.dpToPx
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt


@FragmentScoped
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
        val totalItemCount = parent.layoutManager!!.itemCount
        val lp = view.layoutParams as GridLayoutManager.LayoutParams
        val spanIndex = lp.spanIndex
        //Log.e("GridSpanCountTwoDecorator", "getItemOffsets: pos: $position totalCount: $totalItemCount sI: $spanIndex", )
        if (position == 0 || position == 1) {
            Log.e("GridSpanCountTwoDecorator", "getItemOffsets: $position 젤 위에", )
            outRect.top = dpToPx(context, 16).roundToInt()
        } else {
            Log.e("GridSpanCountTwoDecorator", "getItemOffsets: $position 나머지 탑", )
            outRect.top = dpToPx(context, 32).roundToInt()
        }
        if (spanIndex == 0){
            Log.e("GridSpanCountTwoDecorator", "getItemOffsets: $position 왼쪽", )
            outRect.left = dpToPx(context, 16).roundToInt()
            outRect.right = dpToPx(context, 4).roundToInt()
        }else{
            Log.e("GridSpanCountTwoDecorator", "getItemOffsets: $position 오른쪽", )
            outRect.left = dpToPx(context, 4).roundToInt()
            outRect.right = dpToPx(context, 16).roundToInt()
        }
//        if (position % 2 == 0) {
//            outRect.left = dpToPx(context, 16).roundToInt()
//            outRect.right = dpToPx(context, 4).roundToInt()
//        } else {
//            outRect.left = dpToPx(context, 4).roundToInt()
//            outRect.right = dpToPx(context, 16).roundToInt()
//        }

        if (totalItemCount % 2 == 0) {
            if (position == totalItemCount - 1 || position == totalItemCount - 2) {
                Log.e("GridSpanCountTwoDecorator", "getItemOffsets: $position 아래 두개", )
                outRect.bottom = dpToPx(context, 40).roundToInt()
            }
        } else {
            if (position == totalItemCount - 1) {
                Log.e("GridSpanCountTwoDecorator", "getItemOffsets: $position 아래 한개", )
                outRect.bottom = dpToPx(context, 40).roundToInt()
            }
        }
    }
}