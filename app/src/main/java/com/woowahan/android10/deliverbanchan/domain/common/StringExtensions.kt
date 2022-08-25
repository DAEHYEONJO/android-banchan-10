package com.woowahan.android10.deliverbanchan.domain.common

import android.util.Log

fun String.convertPriceToInt(): Int{
    val noCommaString = replace(",","")
    return if (noCommaString.last() == '원') noCommaString.dropLast(1).toInt()
    else noCommaString.toInt()
}