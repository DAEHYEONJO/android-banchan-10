package com.woowahan.android10.deliverbanchan.presentation.soupdish

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentSoupdishBinding
import com.woowahan.android10.deliverbanchan.presentation.UiState
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.soupdish.adapter.SoupAdapter
import com.woowahan.android10.deliverbanchan.presentation.viewmodel.DishViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SoupDishFragment: BaseFragment<FragmentSoupdishBinding>(R.layout.fragment_soupdish, "SoupDishFragment") {

    private val dishViewModel: DishViewModel by activityViewModels()
    @Inject lateinit var soupAdapter: SoupAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initObserver()
    }

    private fun initObserver() {
        with(dishViewModel){
            getSoupDishes()
            soupState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { state -> handleStateChange(state) }
                .launchIn(lifecycleScope)
        }
    }

    private fun handleStateChange(state: UiState) {
        // todo state 에 따른 처리 추가 구현 필요함
        when(state){
            is UiState.Success -> soupAdapter.submitList(state.uiDishItems)
        }
    }

    private fun initRecyclerView() {
        binding.soupDishRv.adapter = soupAdapter
    }
}