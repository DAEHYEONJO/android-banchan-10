package com.woowahan.android10.deliverbanchan.utils

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.woowahan.android10.deliverbanchan.R
import java.text.DecimalFormat

@BindingAdapter("paintFlag", "nPrice")
fun applyNPriceTextView(view: TextView, nPrice: Int) {
    val decimalFormat = DecimalFormat("#,###")
    view.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    view.text = view.context.getString(R.string.format_nprice, decimalFormat.format(nPrice))
}