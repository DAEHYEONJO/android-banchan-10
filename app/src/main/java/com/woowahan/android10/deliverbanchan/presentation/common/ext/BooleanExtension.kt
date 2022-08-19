package com.woowahan.android10.deliverbanchan.presentation.common.ext

fun Boolean.toInt(): Int{
    return if (this) 1 else 0
}