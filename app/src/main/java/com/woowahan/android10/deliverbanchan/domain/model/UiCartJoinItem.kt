package com.woowahan.android10.deliverbanchan.domain.model

data class UiCartJoinItem(
    val hash: String,
    val title: String,
    var amount: Int,
    var checked: Boolean,
    var nPrice: Int = 0,
    var sPrice: Int,
    val image: String,
    var totalPrice: Int = sPrice*amount
){
    companion object{
        fun emptyItem(): UiCartJoinItem{
            return UiCartJoinItem(
                "",
                "",
                0,
                false,
                0,
                0,
                "",
                0
            )
        }
    }
}
