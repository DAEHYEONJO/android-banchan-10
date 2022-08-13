package com.woowahan.android10.deliverbanchan.presentation.main.sidedish

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentSidedishBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.common.dpToPx
import com.woowahan.android10.deliverbanchan.presentation.common.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.toVisible
import com.woowahan.android10.deliverbanchan.presentation.main.soupdish.SoupAdapter
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import com.woowahan.android10.deliverbanchan.presentation.view.SortSpinnerAdapter
import com.woowahan.android10.deliverbanchan.presentation.view.SpinnerEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SideDishFragment: BaseFragment<FragmentSidedishBinding>(R.layout.fragment_sidedish, "SideDishFragment") {

    private val sideDishViewModel: SideDishViewModel by activityViewModels()
    @Inject lateinit var sideDishAdapter: SoupAdapter
    @Inject lateinit var sideDishSpinnerAdapter: SortSpinnerAdapter
    private val itemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(
            p0: AdapterView<*>?,
            p1: View?,
            position: Int,
            id: Long
        ) {
            with(sideDishViewModel){
                sortSoupDishes(position)
                with(sideDishSpinnerAdapter){
                    sortSpinnerList[curSideDishSpinnerPosition.value!!].selected = true
                    if (curSideDishSpinnerPosition.value!=preSideDishSpinnerPosition.value){
                        sortSpinnerList[preSideDishSpinnerPosition.value!!].selected = false
                    }
                    notifyDataSetChanged()
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            // nothing to do
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initObserver()
    }

    private fun initObserver() {
        with(sideDishViewModel){
            sideState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { state ->
                    Log.e(TAG, "initObserver: $state", )
                    handleStateChange(state)
                }.launchIn(lifecycleScope)
        }
    }

    private fun handleStateChange(state: UiState) {
        when(state){
            is UiState.IsLoading -> binding.sideDishPb.toVisible()
            is UiState.Success -> {
                binding.sideDishPb.toGone()
                sideDishAdapter.submitList(state.uiDishItems)
            }
            is UiState.ShowToast -> {
                binding.sideDishPb.toGone()
                requireContext().showToast(state.message)
            }
        }
    }

    private fun initLayout() {
        with(binding){
            sideDishRv.adapter = sideDishAdapter
            with(sideDishSp){
                setWillNotDraw(false)
                adapter = sideDishSpinnerAdapter.apply {
                    setSpinnerEventsListener(SpinnerEventListener(requireContext()))
                    onItemSelectedListener = itemSelectedListener
                }
            }
        }
    }
}