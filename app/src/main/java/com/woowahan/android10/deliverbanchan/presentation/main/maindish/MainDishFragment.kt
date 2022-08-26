package com.woowahan.android10.deliverbanchan.presentation.main.maindish

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentMaindishBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.base.listeners.SpinnerEventListener
import com.woowahan.android10.deliverbanchan.presentation.common.decorator.GridSpanCountTwoForMainDishDecorator
import com.woowahan.android10.deliverbanchan.presentation.common.ext.observeItemRangeMoved
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.detail.DetailActivity
import com.woowahan.android10.deliverbanchan.presentation.dialogs.bottomsheet.CartBottomSheetFragment
import com.woowahan.android10.deliverbanchan.presentation.main.common.MainGridAdapter
import com.woowahan.android10.deliverbanchan.presentation.main.maindish.adapter.MainDishLinearAdapter
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import com.woowahan.android10.deliverbanchan.presentation.view.adapter.SortSpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainDishFragment :
    BaseFragment<FragmentMaindishBinding>(R.layout.fragment_maindish, "MainDishFragment") {

    private val mainDishViewModel: MainDishViewModel by viewModels()
    private lateinit var mainDishLinearAdapter: MainDishLinearAdapter

    @Inject
    lateinit var mainDishAdapter: MainGridAdapter

    @Inject
    lateinit var mainDishSpinnerAdapter: SortSpinnerAdapter

    @Inject
    lateinit var gridSpanCountTwoForMainDecorator: GridSpanCountTwoForMainDishDecorator

    private val itemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            p0: AdapterView<*>?,
            p1: View?,
            position: Int,
            id: Long
        ) {
            with(mainDishViewModel) {
                sortMainDishes(position)
                with(mainDishSpinnerAdapter) {
                    sortSpinnerList[curMainSpinnerPosition.value!!].selected = true
                    if (curMainSpinnerPosition.value != preMainSpinnerPosition.value) {
                        sortSpinnerList[preMainSpinnerPosition.value!!].selected = false
                    }
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            // nothing to do
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setRadioGroupListener()
        setRecyclerView()
        setSpinnerAdapter()
        setErrorBtn()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        checkErrorState()
    }

    private fun setRadioGroupListener() {
        binding.maindishRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.maindish_rb_grid -> {
                    binding.maindishRv.apply {
                        adapter = mainDishAdapter
                        layoutManager = GridLayoutManager(requireContext(), 2)
                        addItemDecoration(gridSpanCountTwoForMainDecorator)
                    }
                    //mainDishAdapter.submitList(mainDishViewModel.mainDishList.toList())
                }
                R.id.maindish_rb_linear -> {
                    binding.maindishRv.apply {
                        adapter = mainDishLinearAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                        removeItemDecoration(gridSpanCountTwoForMainDecorator)
                    }
                    // mainDishLinearAdapter.submitList(mainDishViewModel.mainDishList.toList())
                }
            }
        }
    }

    private fun setRecyclerView() {
        mainDishAdapter.apply {
            onDishItemClickListener = this@MainDishFragment

            observeItemRangeMoved().flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
                binding.maindishRv.scrollToPosition(0)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

        mainDishLinearAdapter = MainDishLinearAdapter({
            val cartBottomSheetFragment = CartBottomSheetFragment()
            val bundle = Bundle()
            bundle.putParcelable("UiDishItem", it)
            cartBottomSheetFragment.arguments = bundle
            cartBottomSheetFragment.show(childFragmentManager, "CartBottomSheet")
        }, {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("UiDishItem", it)
            startActivity(intent)
        }).apply {
            observeItemRangeMoved().flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
                binding.maindishRv.scrollToPosition(0)
            }.launchIn(lifecycleScope)
        }

        binding.maindishRv.apply {
            adapter = mainDishAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            if (itemDecorationCount == 0) addItemDecoration(gridSpanCountTwoForMainDecorator)
        }


    }

    private fun setSpinnerAdapter() {
        with(binding) {
            with(mainDishSp) {
                setWillNotDraw(false)
                adapter = mainDishSpinnerAdapter.apply {
                    setSpinnerEventsListener(SpinnerEventListener(requireContext()))
                    onItemSelectedListener = itemSelectedListener
                }
            }
        }
    }

    private fun initObserver() {
        mainDishViewModel.mainDishState.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            handleStateChange(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleStateChange(state: UiState<List<UiDishItem>>) {
        when (state) {
            is UiState.Loading -> {
                binding.maindishPb.toVisible()
            }
            is UiState.Success -> {
                binding.maindishPb.toGone()
                binding.errorLayout.errorCl.toGone()
                binding.mainDishAbl.toVisible()
                binding.maindishCdl.toVisible()
                mainDishAdapter.submitList(state.items)
                mainDishLinearAdapter.submitList(state.items)
            }
            is UiState.Error -> {
                binding.maindishPb.toGone()
                binding.maindishCdl.toGone()
                binding.errorLayout.errorCl.toVisible()
            }
        }
    }

    private fun setErrorBtn() {
        binding.errorLayout.errorBtn.setClickEventWithDuration(coroutineScope = viewLifecycleOwner.lifecycleScope) {
            mainDishViewModel.getMainDishList()
        }
    }

    private fun checkErrorState() {
        if (mainDishViewModel.mainDishState.value is UiState.Error) {
            mainDishViewModel.getMainDishList()
        }
    }
}
