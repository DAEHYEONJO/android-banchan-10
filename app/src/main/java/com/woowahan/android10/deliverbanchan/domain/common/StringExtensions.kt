package com.woowahan.android10.deliverbanchan.domain.common

fun String.convertPriceToInt(): Int{
    return this.dropLast(1).replace(",", "").toInt()
}