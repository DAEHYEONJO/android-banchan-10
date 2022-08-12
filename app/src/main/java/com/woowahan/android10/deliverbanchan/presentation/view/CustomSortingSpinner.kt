package com.woowahan.android10.deliverbanchan.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import org.json.JSONObject

class CustomSortingSpinner: AppCompatSpinner {

    constructor(context: Context?) : super(context!!)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    interface OnSpinnerEventsListener {
        fun opPopUpWindowOpened(spinner: Spinner)
        fun onPopUpWindowClosed(spinner: Spinner)
    }

    private var onSpinnerEventsListener: OnSpinnerEventsListener? = null
    private var isOpenInitiated = false

    fun setSpinnerEventsListener(listener: OnSpinnerEventsListener){
        onSpinnerEventsListener = listener
    }

    override fun performClick(): Boolean {
        isOpenInitiated = true
        onSpinnerEventsListener?.opPopUpWindowOpened(this)
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (isOpenInitiated && hasWindowFocus){
            isOpenInitiated = false
            onSpinnerEventsListener?.onPopUpWindowClosed(this)
        }
    }

}