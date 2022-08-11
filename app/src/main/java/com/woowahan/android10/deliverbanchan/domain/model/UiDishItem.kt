package com.woowahan.android10.deliverbanchan.domain.model

data class UiDishItem(
    val hash: String,
    val title: String,
    val isInserted: Boolean,
    val image: String,
    val description: String,
    val sPrice: Int,
    val nPrice: Int = 0,
    val salePercentage: Int = 0,
    val index: Int = 0
)
