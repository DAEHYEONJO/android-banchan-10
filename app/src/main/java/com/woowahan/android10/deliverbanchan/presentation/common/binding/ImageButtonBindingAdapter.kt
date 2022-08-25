package com.woowahan.android10.deliverbanchan.presentation.common.binding

import android.widget.ImageButton
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.woowahan.android10.deliverbanchan.R

@BindingAdapter("app:setBackgroundIcon")
fun ImageButton.setBackgroundIcon(isInserted: Boolean) {
    background = if (isInserted) ResourcesCompat.getDrawable(
        resources,
        R.drawable.btn_cart_inserted_32dp,
        null
    ) else
        ResourcesCompat.getDrawable(resources, R.drawable.btn_cart_32dp, null)
}