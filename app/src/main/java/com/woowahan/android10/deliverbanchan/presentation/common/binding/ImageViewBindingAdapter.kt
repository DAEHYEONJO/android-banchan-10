package com.woowahan.android10.deliverbanchan.presentation.common.binding

import android.util.Log
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.woowahan.android10.deliverbanchan.R

@BindingAdapter("app:setStringUrlImage")
fun ImageView.setStringUrlImage(stringUrl: String) {
    Glide.with(this.context)
        .load(stringUrl)
        .into(this)
}

@BindingAdapter("app:setCartIcon")
fun ImageView.setCartIcon(cartIconText: String) {
    Log.e("BindingAdapter", "setCartIcon: $cartIconText")
    background = if (cartIconText != "") ResourcesCompat.getDrawable(
        resources,
        R.drawable.ic_cart_no_right_top_margin_0,
        null
    )
    else ResourcesCompat.getDrawable(resources, R.drawable.ic_cart_margin_0, null)
}

@BindingAdapter("app:setProfileIcon")
fun ImageView.setProfileIcon(isOrderExist: Boolean) {
    background =
        if (isOrderExist) ResourcesCompat.getDrawable(resources, R.drawable.ic_user_badge, null)
        else ResourcesCompat.getDrawable(resources, R.drawable.ic_user_without_badge, null)
}
