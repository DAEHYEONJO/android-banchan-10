package com.woowahan.android10.deliverbanchan.presentation.common.ext

import android.content.Context
import android.util.DisplayMetrics

fun dpToPx(context: Context, dp: Int): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}