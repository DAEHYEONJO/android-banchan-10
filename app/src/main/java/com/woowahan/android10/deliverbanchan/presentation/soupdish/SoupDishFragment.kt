package com.woowahan.android10.deliverbanchan.presentation.soupdish

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentSoupdishBinding
import com.woowahan.android10.deliverbanchan.presentation.view.model.UiState
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.soupdish.adapter.SoupAdapter
import com.woowahan.android10.deliverbanchan.presentation.view.CustomSortingSpinner
import com.woowahan.android10.deliverbanchan.presentation.view.adapter.SortSpinnerAdapter
import com.woowahan.android10.deliverbanchan.presentation.viewmodel.DishViewModel
import com.woowahan.android10.deliverbanchan.utils.converter.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SoupDishFragment: BaseFragment<FragmentSoupdishBinding>(R.layout.fragment_soupdish, "SoupDishFragment") {

    private val dishViewModel: DishViewModel by activityViewModels()
    @Inject lateinit var soupAdapter: SoupAdapter
    @Inject lateinit var soupSpinnerAdapter: SortSpinnerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated:selectedItemPosition ${binding.soupDishSp.selectedItemPosition}", )
        initLayout()
        initObserver()
    }

    private fun initObserver() {
        with(dishViewModel){
            getSoupDishes()
            soupState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { state ->
                    handleStateChange(state)
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun handleStateChange(state: UiState) {
        // todo state 에 따른 처리 추가 구현 필요함
        when(state){
            is UiState.Success -> {
                soupAdapter.submitList(state.uiDishItems)
                binding.soupPb.visibility = View.GONE
            }
            is UiState.IsLoading -> binding.soupPb.visibility = View.VISIBLE
        }
    }

    private fun initLayout() {
        with(binding){
            soupDishRv.adapter = soupAdapter
            with(soupDishSp){
                dropDownVerticalOffset = dpToPx(requireContext(), 32).toInt()
                setWillNotDraw(false)
                adapter = soupSpinnerAdapter.apply {
                    setSpinnerEventsListener(object : CustomSortingSpinner.OnSpinnerEventsListener{
                        override fun opPopUpWindowOpened(spinner: Spinner) {
                            spinner.background = AppCompatResources.getDrawable(requireContext(), R.drawable.bg_sort_spinner_up)
                        }

                        override fun onPopUpWindowClosed(spinner: Spinner) {
                            spinner.background = AppCompatResources.getDrawable(requireContext(), R.drawable.bg_sort_spinner_down)
                        }
                    })
                    onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            position: Int,
                            id: Long
                        ) {
                            //val pos = if (dishViewModel.soupSpinnerPosition.value!=0) dishViewModel.soupSpinnerPosition.value else position
                            soupSpinnerAdapter.sortSpinnerList.forEach {
                                android.util.Log.e(TAG, "before onItemSelected: $it", )
                            }
                            Log.e(TAG, "before: vm: ${dishViewModel.soupSpinnerPosition.value} pre: ${preSelectedPosition} cur: ${curSelectedPosition} pos: ${position}", )
                            sortSpinnerList[position].selected = true
                            if (preSelectedPosition!=-1)
                                sortSpinnerList[curSelectedPosition].selected = false
                            preSelectedPosition = curSelectedPosition
                            curSelectedPosition = position
                            setSelection(position)
                            dishViewModel.sortSoupDishes(position)
                            notifyDataSetChanged()
                            soupSpinnerAdapter.sortSpinnerList.forEach {
                                android.util.Log.e(TAG, "after onItemSelected: $it", )
                            }
                            Log.e(TAG, "after: vm: ${dishViewModel.soupSpinnerPosition.value} pre: ${preSelectedPosition} cur: ${curSelectedPosition} pos: ${position}", )
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }
                }
            }
        }
    }
}