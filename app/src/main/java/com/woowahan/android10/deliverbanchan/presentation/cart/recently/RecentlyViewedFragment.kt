package com.woowahan.android10.deliverbanchan.presentation.cart.recently

import android.os.Bundle
import android.view.View
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentRecentlyViewedBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentlyViewedFragment : BaseFragment<FragmentRecentlyViewedBinding>(
    R.layout.fragment_recently_viewed,
    "RecentlyViewedFragment"
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}