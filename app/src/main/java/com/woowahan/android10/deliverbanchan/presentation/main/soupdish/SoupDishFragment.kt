package com.woowahan.android10.deliverbanchan.presentation.main.soupdish

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentSoupdishBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.base.listeners.SpinnerEventListener
import com.woowahan.android10.deliverbanchan.presentation.common.decorator.GridSpanCountTwoForMainDishDecorator
import com.woowahan.android10.deliverbanchan.presentation.common.ext.observeItemRangeMoved
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.main.common.MainGridAdapter
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import com.woowahan.android10.deliverbanchan.presentation.view.adapter.SortSpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SoupDishFragment :
    BaseFragment<FragmentSoupdishBinding>(R.layout.fragment_soupdish, "SoupDishFragment") {

    private val soupViewModel: SoupViewModel by activityViewModels()

    @Inject
    lateinit var mainGridAdapter: MainGridAdapter

    @Inject
    lateinit var soupSpinnerAdapter: SortSpinnerAdapter

    @Inject
    lateinit var gridSpanCountTwoForMainDishDecorator: GridSpanCountTwoForMainDishDecorator
    private val itemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            p0: AdapterView<*>?,
            p1: View?,
            position: Int,
            id: Long
        ) {
            with(soupViewModel) {
                sortSoupDishes(position)
                with(soupSpinnerAdapter) {
                    sortSpinnerList[curSoupSpinnerPosition.value!!].selected = true
                    if (curSoupSpinnerPosition.value != preSoupSpinnerPosition.value) {
                        sortSpinnerList[preSoupSpinnerPosition.value!!].selected = false
                    }
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            // nothing to do
        }
    }

    override fun onResume() {
        super.onResume()
        checkErrorState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = soupViewModel
        initLayout()
        setErrorBtn()
        initObserver()
    }

    private fun initObserver() {
        with(soupViewModel) {
            soupState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { state ->
                    handleStateChange(state)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

        }
    }

    private fun handleStateChange(state: UiState<List<UiDishItem>>) {
        when (state) {
            is UiState.Loading -> {
                binding.soupPb.toVisible()
            }
            is UiState.Success -> {
                binding.soupPb.toGone()
                binding.errorLayout.errorCl.toGone()
                binding.soupDishAbl.toVisible()
                binding.soupCdl.toVisible()
                mainGridAdapter.submitList(state.items)
            }
            is UiState.Error -> {
                binding.soupPb.toGone()
                binding.soupCdl.toGone()
                binding.errorLayout.errorCl.toVisible()
            }
        }
    }

    private fun initLayout() {
        with(binding) {
            with(soupDishRv) {
                adapter = mainGridAdapter.apply {
                    onDishItemClickListener = this@SoupDishFragment
                }
                if (itemDecorationCount == 0) addItemDecoration(gridSpanCountTwoForMainDishDecorator)
            }
            with(soupDishSp) {
                setWillNotDraw(false)
                adapter = soupSpinnerAdapter.apply {
                    setSpinnerEventsListener(SpinnerEventListener(requireContext()))
                    onItemSelectedListener = itemSelectedListener
                }
            }
        }

        mainGridAdapter.apply {
            observeItemRangeMoved().flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
                binding.soupDishRv.scrollToPosition(0)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun setErrorBtn() {
        binding.errorLayout.errorBtn.setClickEventWithDuration(coroutineScope = viewLifecycleOwner.lifecycleScope) {
            soupViewModel.setSoupDishesState()
        }
    }

    private fun checkErrorState() {
        if (soupViewModel.soupState.value is UiState.Error) {
            soupViewModel.setSoupDishesState()
        }
    }
}