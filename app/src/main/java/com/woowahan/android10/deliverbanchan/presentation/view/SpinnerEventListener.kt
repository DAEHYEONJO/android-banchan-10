package com.woowahan.android10.deliverbanchan.presentation.view

import android.app.Application
import android.content.Context
import android.widget.Spinner
import androidx.appcompat.content.res.AppCompatResources
import com.woowahan.android10.deliverbanchan.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpinnerEventListener @Inject constructor(
    @ApplicationContext private val context:Context
) : CustomSortingSpinner.OnSpinnerEventsListener {
    override fun opPopUpWindowOpened(spinner: Spinner) {
        spinner.background =
            AppCompatResources.getDrawable(context, R.drawable.bg_sort_spinner_up)
    }

    override fun onPopUpWindowClosed(spinner: Spinner) {
        spinner.background =
            AppCompatResources.getDrawable(context, R.drawable.bg_sort_spinner_down)
    }
}