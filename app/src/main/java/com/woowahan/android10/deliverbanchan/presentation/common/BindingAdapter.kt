package com.woowahan.android10.deliverbanchan.presentation.common

import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.text.DecimalFormat

@BindingAdapter("app:setStringUrlImage")
fun ImageView.setStringUrlImage(stringUrl: String) {
    Glide.with(this.context)
        .load(stringUrl)
        .into(this)
}

@BindingAdapter("price","isNPrice")
fun TextView.setPriceText(price: Int, isNPrice: Boolean) {
    text = if (price == 0) "" else {
        val formatter = DecimalFormat("###,###")
        "${formatter.format(price)}원"
    }
    if (isNPrice) paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
}
