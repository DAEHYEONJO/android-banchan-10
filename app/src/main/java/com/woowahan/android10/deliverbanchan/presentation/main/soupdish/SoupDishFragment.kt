package com.woowahan.android10.deliverbanchan.presentation.main.soupdish

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.data.local.model.CartInfo
import com.woowahan.android10.deliverbanchan.databinding.FragmentSoupdishBinding
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.view.CustomSortingSpinner
import com.woowahan.android10.deliverbanchan.presentation.view.SortSpinnerAdapter
import com.woowahan.android10.deliverbanchan.presentation.main.host.DishViewModel
import com.woowahan.android10.deliverbanchan.presentation.common.dpToPx
import com.woowahan.android10.deliverbanchan.presentation.common.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.toVisible
import com.woowahan.android10.deliverbanchan.presentation.state.UiCartState
import com.woowahan.android10.deliverbanchan.presentation.view.SpinnerEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SoupDishFragment: BaseFragment<FragmentSoupdishBinding>(R.layout.fragment_soupdish, "SoupDishFragment") {

    private val soupViewModel: SoupViewModel by activityViewModels()
    @Inject lateinit var soupAdapter: SoupAdapter
    @Inject lateinit var soupSpinnerAdapter: SortSpinnerAdapter
    private val itemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(
            p0: AdapterView<*>?,
            p1: View?,
            position: Int,
            id: Long
        ) {
            with(soupViewModel){
                sortSoupDishes(position)
                with(soupSpinnerAdapter){
                    sortSpinnerList[curSoupSpinnerPosition.value!!].selected = true
                    if (curSoupSpinnerPosition.value!=preSoupSpinnerPosition.value){
                        sortSpinnerList[preSoupSpinnerPosition.value!!].selected = false
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
        binding.vm = soupViewModel
        initLayout()
        initObserver()
    }

    private fun initObserver() {
        with(soupViewModel){
            soupState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { state ->
                    Log.e(TAG, "initObserver: $state", )
                    handleStateChange(state)
                }.launchIn(lifecycleScope)
        }
    }

    private fun handleStateChange(state: UiState) {
        // todo state 에 따른 처리 추가 구현 필요함
        when(state){
            is UiState.IsLoading -> binding.soupPb.toVisible()
            is UiState.Success -> {
                binding.soupPb.toGone()
                soupAdapter.submitList(state.uiDishItems)
            }
            is UiState.ShowToast -> {
                binding.soupPb.toGone()
                requireContext().showToast(state.message)
            }
        }
    }

    private fun initLayout() {
        with(binding){
            soupDishRv.adapter = soupAdapter
            with(soupDishSp){
                setWillNotDraw(false)
                adapter = soupSpinnerAdapter.apply {
                    setSpinnerEventsListener(SpinnerEventListener(requireContext()))
                    onItemSelectedListener = itemSelectedListener
                }
            }
        }
    }
}