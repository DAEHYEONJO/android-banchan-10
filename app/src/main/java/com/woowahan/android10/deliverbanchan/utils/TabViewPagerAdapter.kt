package com.woowahan.android10.deliverbanchan.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.woowahan.android10.deliverbanchan.presentation.exhibition.ExhibitionFragment
import com.woowahan.android10.deliverbanchan.presentation.maindish.MainDishFragment
import com.woowahan.android10.deliverbanchan.presentation.sidedish.SideDishFragment
import com.woowahan.android10.deliverbanchan.presentation.soupdish.SoupDishFragment

private const val NUM_TABS = 4

class TabViewPagerAdapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifeCycle) {

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ExhibitionFragment()
            1 -> MainDishFragment()
            2 -> SoupDishFragment()
            else -> SideDishFragment()
        }
    }
}