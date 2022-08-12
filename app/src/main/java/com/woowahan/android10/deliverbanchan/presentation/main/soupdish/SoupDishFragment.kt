package com.woowahan.android10.deliverbanchan.presentation.main.soupdish

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
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.view.CustomSortingSpinner
import com.woowahan.android10.deliverbanchan.presentation.view.SortSpinnerAdapter
import com.woowahan.android10.deliverbanchan.presentation.main.host.DishViewModel
import com.woowahan.android10.deliverbanchan.presentation.common.dpToPx
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
        Log.e(TAG, "onViewCreated binding.soupDishTvItemCount.paintFlags: ${binding.soupDishTvItemCount.paintFlags}", )
    }

    private fun initObserver() {
        with(dishViewModel){
            getSoupDishes()
            soupState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { state ->
                    Log.e(TAG, "initObserver: $state", )
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
                            soupSpinnerAdapter.sortSpinnerList.forEach {
                                Log.e(TAG, "before onItemSelected: $it", )
                            }
                            with(dishViewModel){
                                sortSoupDishes(position)
                                soupSpinnerAdapter.sortSpinnerList[curSoupSpinnerPosition.value!!].selected = true
                                if (curSoupSpinnerPosition.value!=preSoupSpinnerPosition.value){
                                    soupSpinnerAdapter.sortSpinnerList[preSoupSpinnerPosition.value!!].selected = false
                                }
                                notifyDataSetChanged()
                            }

                            soupSpinnerAdapter.sortSpinnerList.forEach {
                                Log.e(TAG, "after onItemSelected: $it", )
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }
                }
            }
        }
    }
}