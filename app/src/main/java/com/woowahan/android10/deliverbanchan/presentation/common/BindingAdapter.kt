package com.woowahan.android10.deliverbanchan.presentation.common

import android.graphics.Paint
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.woowahan.android10.deliverbanchan.R
import org.w3c.dom.Text
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

@BindingAdapter("app:setCartIcon")
fun ImageView.setCartIcon(cartIconText: String){
    Log.e("BindingAdapter", "setCartIcon: $cartIconText", )
    background = if (cartIconText!="") ResourcesCompat.getDrawable(resources, R.drawable.ic_cart_no_right_top_margin_0, null)
    else ResourcesCompat.getDrawable(resources, R.drawable.ic_cart_margin_0, null)
}

@BindingAdapter("app:setCartTextViewVisibility")
fun TextView.setCartTextViewVisibility(cartIconText: String){
    Log.e("BindingAdapter", "setCartTextViewVisibility: $cartIconText", )
    visibility = if (cartIconText=="") View.GONE
    else View.VISIBLE
}

@BindingAdapter("app:setBottomSheetButtonText")
fun Button.setBottomSheetButtonText(itemCount: Int) {
    text = "${itemCount}개 담기"
}