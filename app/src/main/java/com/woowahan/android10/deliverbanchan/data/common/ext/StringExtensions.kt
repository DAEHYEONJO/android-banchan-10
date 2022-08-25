package com.woowahan.android10.deliverbanchan.data.common.ext

fun String.convertPriceToInt(): Int{
    val noCommaString = replace(",","")
    return if (noCommaString.last() == '원') noCommaString.dropLast(1).toInt()
    else noCommaString.toInt()
}