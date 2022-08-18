package com.woowahan.android10.deliverbanchan.presentation.common

import android.graphics.Paint
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginTop
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

@BindingAdapter("app:setDeliveryPriceText")
fun TextView.setDeliveryPriceText(price: Int){
    text = price.convertPriceToString()
}

@BindingAdapter("price", "isNPrice")
fun TextView.setPriceText(price: Int, isNPrice: Boolean) {
    text = if (price == 0) "" else {
        price.convertPriceToString()
    }
    if (isNPrice) paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
}

@BindingAdapter("app:setBackgroundIcon")
fun ImageButton.setBackgroundIcon(isInserted: Boolean) {
    background = if (isInserted) ResourcesCompat.getDrawable(
        resources,
        R.drawable.btn_cart_inserted_32dp,
        null
    ) else
        ResourcesCompat.getDrawable(resources, R.drawable.btn_cart_32dp, null)
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

@BindingAdapter("app:setCartTextViewVisibility")
fun TextView.setCartTextViewVisibility(cartIconText: String) {
    Log.e("BindingAdapter", "setCartTextViewVisibility: $cartIconText")
    visibility = if (cartIconText == "") View.GONE
    else View.VISIBLE
}

@BindingAdapter("app:setBottomSheetButtonText")
fun Button.setBottomSheetButtonText(itemCount: Int) {
    text = "${itemCount}개 담기"
}

@BindingAdapter("app:setProfileIcon")
fun ImageView.setProfileIcon(isOrderExist: Boolean) {
    background = if (isOrderExist) ResourcesCompat.getDrawable(resources, R.drawable.ic_user_badge, null)
        else ResourcesCompat.getDrawable(resources, R.drawable.ic_user_without_badge, null)
}

@BindingAdapter("app:getTimeString")
fun TextView.getTimeString(beforeTime: Long){
    val curTime = System.currentTimeMillis()
    var diffTime = (curTime - beforeTime)/1000
    val timeUnitArray = intArrayOf(60, 60, 24, 30, 12)
    val timeSuffixArray = arrayOf("방금전","분전","시간전","일전","달전","년전")

    if (diffTime < timeUnitArray[0]){
        text = timeSuffixArray[0]
        return
    }

    repeat (4){ index ->
        diffTime /= timeUnitArray[index]
        if (diffTime < timeUnitArray[index+1]){
            text = resources.getString(R.string.time_format, diffTime.toString(), timeSuffixArray[index+1])
            return
        }
    }

    text = resources.getString(R.string.time_format, (diffTime/timeUnitArray.last()).toString(), timeSuffixArray.last())
}

@BindingAdapter("isAvailableDelivery", "totalPrice")
fun TextView.setOrderBtnText(isAvailableDelivery: Boolean, totalPrice: Int){
    text = if (isAvailableDelivery) {
        resources.getString(R.string.order_format, totalPrice.convertPriceToString())
    }else{
        resources.getString(R.string.item_cart_bottom_body_btn)
    }
}

@BindingAdapter("app:setDeliveryForFreeText")
fun TextView.setDeliveryForFreeText(price: Int){
    text = resources.getString(R.string.order_form_free_delivery_format, price.convertPriceToString())
}

@BindingAdapter("isAvailableDelivery","isAvailableDeliveryFree")
fun TextView.setDeliveryForFreeTextVisibility(isAvailableDelivery: Boolean, isAvailableDeliveryFree: Boolean){
    val deliveryBit = isAvailableDelivery.toInt()
    val deliveryFreeBit = isAvailableDeliveryFree.toInt()
    val bitMask = (deliveryBit shl 1) or deliveryFreeBit
    if (bitMask == 3 || bitMask == 0) toGone() else toVisible()
}