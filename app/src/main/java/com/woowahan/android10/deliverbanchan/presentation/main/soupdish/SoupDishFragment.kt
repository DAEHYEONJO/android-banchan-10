package com.woowahan.android10.deliverbanchan.presentation.main.soupdish

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentSoupdishBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.common.decorator.GridSpanCountTwoDecorator
import com.woowahan.android10.deliverbanchan.presentation.view.adapter.SortSpinnerAdapter
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.main.common.MainGridAdapter
import com.woowahan.android10.deliverbanchan.presentation.main.host.DishViewModel
import com.woowahan.android10.deliverbanchan.presentation.state.UiTempState
import com.woowahan.android10.deliverbanchan.presentation.view.SpinnerEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SoupDishFragment :
    BaseFragment<FragmentSoupdishBinding>(R.layout.fragment_soupdish, "SoupDishFragment"),
    ViewTreeObserver.OnGlobalLayoutListener {

    override fun onGlobalLayout() {
        binding.soupDishRv.scrollToPosition(0)
    }

    private val dishViewModel: DishViewModel by activityViewModels()
    private val soupViewModel: SoupViewModel by activityViewModels()

    @Inject
    lateinit var mainGridAdapter: MainGridAdapter

    @Inject
    lateinit var soupSpinnerAdapter: SortSpinnerAdapter

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
            with(soupViewModel) {
                sortSoupDishes(position)
                with(soupSpinnerAdapter) {
                    sortSpinnerList[curSoupSpinnerPosition.value!!].selected = true
                    if (curSoupSpinnerPosition.value != preSoupSpinnerPosition.value) {
                        sortSpinnerList[preSoupSpinnerPosition.value!!].selected = false
                    }
//                    if (!isListenerAdd) {
//                        isListenerAdd = true
//                        binding.soupDishRv.viewTreeObserver.addOnGlobalLayoutListener(this@SoupDishFragment)
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
        binding.vm = soupViewModel
        initLayout()
        setErrorBtn()
        initObserver()
    }

    private fun initObserver() {
        with(soupViewModel) {
            soupState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { state ->
                    Log.e(TAG, "initObserver: $state")
                    handleStateChange(state)
                }.launchIn(lifecycleScope)

        }
    }

    private fun handleStateChange(state: UiTempState<List<UiDishItem>>) {
        when (state) {
            is UiTempState.Loading -> {
                binding.soupPb.toVisible()
                binding.errorLayout.errorCl.toGone()
            }
            is UiTempState.Success -> {
                binding.soupPb.toGone()
                binding.soupCdl.toVisible()
                mainGridAdapter.submitList(state.uiDishItems)
            }
            is UiTempState.ShowToast -> {
                Log.e("SoupDishFragment", "show toast")
                requireContext().showToast(state.message)
            }
            is UiTempState.Error -> {
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
//                addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                        super.onScrollStateChanged(recyclerView, newState)
//                        if (isListenerAdd) {
//                            viewTreeObserver.removeOnGlobalLayoutListener(this@SoupDishFragment)
//                            isListenerAdd = false
//                        }
//                    }
//                })
                if (itemDecorationCount == 0) addItemDecoration(gridSpanCountTwoDecorator)
            }
            with(soupDishSp) {
                setWillNotDraw(false)
                adapter = soupSpinnerAdapter.apply {
                    setSpinnerEventsListener(SpinnerEventListener(requireContext()))
                    onItemSelectedListener = itemSelectedListener
                }
            }
        }
    }

    private fun setErrorBtn() {
        binding.errorLayout.errorBtn.setOnClickListener {
            soupViewModel.setSoupDishesState()
        }
    }

    private fun checkErrorState() {
        if (soupViewModel.soupState.value is UiTempState.Error) {
            soupViewModel.setSoupDishesState()
        }
    }
}