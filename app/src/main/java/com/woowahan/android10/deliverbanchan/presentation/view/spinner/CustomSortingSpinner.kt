package com.woowahan.android10.deliverbanchan.presentation.view.spinner

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.presentation.common.ext.dpToPx

class CustomSortingSpinner(context: Context, attrs: AttributeSet?) :
    AppCompatSpinner(context, attrs) {

    companion object {
        const val TAG = "CustomSortingSpinner"
    }

    private var onSpinnerEventsListener: OnSpinnerEventsListener? = null
    private var isOpenInitiated = false
    private var verticalOffsetDpValue = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSortingSpinner)
        verticalOffsetDpValue = typedArray.getDimensionPixelSize(
            R.styleable.CustomSortingSpinner_verticalOffsetDpValue,
            dpToPx(context, 32).toInt()
        )
        typedArray.recycle()
        dropDownVerticalOffset = verticalOffsetDpValue
    }

    interface OnSpinnerEventsListener {
        fun opPopUpWindowOpened(spinner: Spinner)
        fun onPopUpWindowClosed(spinner: Spinner)
    }

    fun setSpinnerEventsListener(listener: OnSpinnerEventsListener) {
        onSpinnerEventsListener = listener
    }

    override fun performClick(): Boolean {
        isOpenInitiated = true
        onSpinnerEventsListener?.opPopUpWindowOpened(this)
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (isOpenInitiated && hasWindowFocus) {
            isOpenInitiated = false
            onSpinnerEventsListener?.onPopUpWindowClosed(this)
        }
    }
}