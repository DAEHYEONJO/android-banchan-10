package com.woowahan.android10.deliverbanchan.presentation.main.host

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.woowahan.android10.deliverbanchan.presentation.main.exhibition.ExhibitionFragment
import com.woowahan.android10.deliverbanchan.presentation.main.maindish.MainDishFragment
import com.woowahan.android10.deliverbanchan.presentation.main.sidedish.SideDishFragment
import com.woowahan.android10.deliverbanchan.presentation.main.soupdish.SoupDishFragment

private const val NUM_TABS = 4

class TabViewPagerAdapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifeCycle) {

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ExhibitionFragment()
            1 -> SoupDishFragment()
            2 -> SoupDishFragment()
            else -> SideDishFragment()
        }
    }
}