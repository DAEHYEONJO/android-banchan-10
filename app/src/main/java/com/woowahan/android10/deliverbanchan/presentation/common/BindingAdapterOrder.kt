package com.woowahan.android10.deliverbanchan.presentation.common

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.woowahan.android10.deliverbanchan.R

@BindingAdapter("app:setOrderStateText")
fun TextView.setDeliverStateText(isDeliver: Boolean) {
    if (isDeliver) {
        setTextColor(ContextCompat.getColor(this.context, R.color.primary_accent))
        text = "배송 준비중"
    } else {
        setTextColor(ContextCompat.getColor(this.context, R.color.grey_scale_black))
        text = "배송완료"
    }
}

@BindingAdapter("app:setOrderListTitle", "app:setOrderItemSize")
fun TextView.setOrderTitle(orderListTitle: String, orderItemSize: Int) {
    if (orderItemSize <= 1) {
        text = orderListTitle
    } else {
        text = "${orderListTitle} 외 ${orderItemSize - 1}개"
    }
}

@BindingAdapter("app:setAmount")
fun TextView.setAmount(amount: Int) {
    text = "총 ${amount}개"
}