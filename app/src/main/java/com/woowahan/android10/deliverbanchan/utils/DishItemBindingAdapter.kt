package com.woowahan.android10.deliverbanchan.utils

import android.annotation.SuppressLint
import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.woowahan.android10.deliverbanchan.R
import java.text.DecimalFormat

@BindingAdapter("sPrice")
fun applySPriceTextView(view: TextView, nPrice: Int) {
    val decimalFormat = DecimalFormat("#,###")
    view.text = view.context.getString(R.string.format_nprice, decimalFormat.format(nPrice))
}

@BindingAdapter("nPrice")
fun applyNPriceTextView(view: TextView, nPrice: Int) {
    val decimalFormat = DecimalFormat("#,###")
    view.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    view.text = view.context.getString(R.string.format_nprice, decimalFormat.format(nPrice))
    view.isVisible = (nPrice != 0)
}

@BindingAdapter("nPrice", "sPrice")
fun applySaleTextView(view: TextView, nPrice: Int, sPrice: Int) {
    if (nPrice == 0) {
        view.isVisible = false
    } else {
        val sale = ((nPrice - sPrice) / nPrice.toDouble() * 100).toInt()
        val saleStr = view.context.getString(R.string.format_sale_rate, sale)
        view.isVisible = true
        view.text = saleStr
    }
}

@BindingAdapter("imageUrl")
fun applyFoodImageView(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .into(imageView)
}