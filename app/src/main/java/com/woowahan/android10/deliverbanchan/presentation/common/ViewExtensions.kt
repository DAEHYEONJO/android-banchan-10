package com.woowahan.android10.deliverbanchan.presentation.common

import android.content.Context
import android.view.View
import android.widget.Toast

fun View.toGone(){
    visibility = View.GONE
}

fun View.toVisible(){
    visibility = View.VISIBLE
}

fun View.toInvisible(){
    visibility = View.INVISIBLE
}