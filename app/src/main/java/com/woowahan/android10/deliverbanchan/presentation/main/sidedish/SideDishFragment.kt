package com.woowahan.android10.deliverbanchan.presentation.main.sidedish

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentSidedishBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.common.decorator.GridSpanCountTwoDecorator
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.main.common.MainGridAdapter
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import com.woowahan.android10.deliverbanchan.presentation.view.SpinnerEventListener
import com.woowahan.android10.deliverbanchan.presentation.view.adapter.SortSpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SideDishFragment :
    BaseFragment<FragmentSidedishBinding>(R.layout.fragment_sidedish, "SideDishFragment"),
    ViewTreeObserver.OnGlobalLayoutListener {

    override fun onGlobalLayout() {
        binding.sideDishRv.scrollToPosition(0)
    }

    private val sideDishViewModel: SideDishViewModel by activityViewModels()

    @Inject
    lateinit var sideDishAdapter: MainGridAdapter

    @Inject
    lateinit var sideDishSpinnerAdapter: SortSpinnerAdapter

    @Inject
    lateinit var gridSpanCountTwoDecorator: GridSpanCountTwoDecorator
    var isListenerAdd = false
    private val itemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            p0: AdapterView<*>?,
            p1: View?,
            position: Int,
            id: Long
        ) {
            Log.e(TAG, "onItemSelected: $position", )
            with(sideDishViewModel) {
                sortSoupDishes(position)
                with(sideDishSpinnerAdapter) {
                    sortSpinnerList[curSideDishSpinnerPosition.value!!].selected = true
                    if (curSideDishSpinnerPosition.value != preSideDishSpinnerPosition.value) {
                        sortSpinnerList[preSideDishSpinnerPosition.value!!].selected = false
                    }
//                    if (!isListenerAdd) {
//                        isListenerAdd = true
//                        binding.sideDishRv.viewTreeObserver.addOnGlobalLayoutListener(this@SideDishFragment)
//                    }
                    //notifyDataSetChanged()
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            // nothing to do
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "viewLifecycleOwner: ${viewLifecycleOwner}")
        Log.e(TAG, "lifecycleScope: ${viewLifecycleOwner.lifecycleScope}")
        checkErrorState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = sideDishViewModel
        initLayout()
        initObserver()
        setErrorBtn()
    }

    private fun initObserver() {
        with(sideDishViewModel) {
            sideState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach { state ->
                    Log.e(TAG, "initObserver: $state")
                    handleStateChange(state)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun handleStateChange(state: UiState<List<UiDishItem>>) {
        when (state) {
            is UiState.Loading -> {
                binding.sideDishPb.toVisible()
                binding.errorLayout.errorCl.toGone()
            }
            is UiState.Success -> {
                binding.sideDishPb.toGone()
                binding.sideDishCdl.toVisible()
                sideDishAdapter.submitList(state.items)
            }
            is UiState.Error -> {
                binding.sideDishPb.toGone()
                binding.sideDishCdl.toGone()
                binding.errorLayout.errorCl.toVisible()
            }
        }
    }

    private fun initLayout() {
        with(binding) {
            with(sideDishRv) {
                adapter = sideDishAdapter.apply {
                    onDishItemClickListener = this@SideDishFragment
                }
//                addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                        super.onScrollStateChanged(recyclerView, newState)
//                        if (isListenerAdd) {
//                            viewTreeObserver.removeOnGlobalLayoutListener(this@SideDishFragment)
//                            isListenerAdd = false
//                        }
//                    }
//                })
                if (itemDecorationCount == 0) addItemDecoration(gridSpanCountTwoDecorator)
            }
            with(sideDishSp) {
                setWillNotDraw(false)
                adapter = sideDishSpinnerAdapter.apply {
                    setSpinnerEventsListener(SpinnerEventListener(requireContext()))
                    onItemSelectedListener = itemSelectedListener
                }
            }
        }
    }

    private fun setErrorBtn() {
        binding.errorLayout.errorBtn.setOnClickListener {
            sideDishViewModel.getSideDishList()
        }
    }

    private fun checkErrorState() {
        if (sideDishViewModel.sideState.value is UiState.Error) {
            sideDishViewModel.getSideDishList()
        }
    }
}