package com.woowahan.android10.deliverbanchan.presentation.common

import android.graphics.Paint
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.woowahan.android10.deliverbanchan.R
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

@BindingAdapter("app:setBackgroundIcon")
fun ImageButton.setBackgroundIcon(isInserted: Boolean){
    background = if (isInserted) ResourcesCompat.getDrawable(resources, R.drawable.btn_cart_inserted_32dp, null) else
        ResourcesCompat.getDrawable(resources, R.drawable.btn_cart_32dp, null)
}