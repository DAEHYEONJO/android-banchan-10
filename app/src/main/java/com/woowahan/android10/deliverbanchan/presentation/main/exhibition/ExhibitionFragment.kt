package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

import android.os.Bundle
import android.view.View
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentExhibitionBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExhibitionFragment: BaseFragment<FragmentExhibitionBinding>(R.layout.fragment_exhibition, "ExhibitionFragment") {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}