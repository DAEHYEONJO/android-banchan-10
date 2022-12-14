package com.woowahan.android10.deliverbanchan.presentation.common.ext

import java.text.DecimalFormat

fun Int.convertPriceToString(): String {
    val formatter = DecimalFormat("###,###")
    return "${formatter.format(this)}원"
}
