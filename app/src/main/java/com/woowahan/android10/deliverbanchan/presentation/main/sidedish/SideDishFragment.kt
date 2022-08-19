package com.woowahan.android10.deliverbanchan.presentation.main.sidedish

import android.content.Intent
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
import com.woowahan.android10.deliverbanchan.presentation.cart.CartActivity
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.detail.DetailActivity
import com.woowahan.android10.deliverbanchan.presentation.dialogs.bottomsheet.CartBottomSheetFragment
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.CartDialogFragment
import com.woowahan.android10.deliverbanchan.presentation.main.common.MainGridAdapter
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
    @Inject lateinit var sideDishAdapter: MainGridAdapter
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
        binding.vm = sideDishViewModel
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
            sideDishRv.adapter = sideDishAdapter.apply {
                cartIconClick = {
                    val cartBottomSheetFragment = CartBottomSheetFragment()
                    cartBottomSheetFragment.setDialogDismissWhenInsertSuccessListener(object: CartBottomSheetFragment.DialogDismissWhenInsertSuccessListener{
                        override fun dialogDismissWhenInsertSuccess(hash: String, title: String) {
                            sideDishViewModel.changeSoupItemIsInserted(hash)
                            val cartDialog = CartDialogFragment()

                            cartDialog.setTextClickListener(object :
                                CartDialogFragment.TextClickListener {
                                override fun moveToCartTextClicked(hash: String, title: String) {
                                    Log.e(
                                        "SideDishFragment",
                                        "move to cart, hash : ${hash}, title : ${title}"
                                    )
                                    startActivity(Intent(requireActivity(), CartActivity::class.java))
                                }
                            })

                            val bundle = Bundle()
                            bundle.putString("hash", hash)
                            bundle.putString("title", title)
                            cartDialog.arguments = bundle
                            cartDialog.show(childFragmentManager, "CartDialog")
                            Log.e(TAG, "현재 선택된 상품명 : ${title}")
                        }
                    })
                    val bundle = Bundle()
                    bundle.putParcelable("UiDishItem", it)
                    cartBottomSheetFragment.arguments = bundle
                    cartBottomSheetFragment.show(childFragmentManager, "CartBottomSheet")
                }

                itemClick = {
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra("UiDishItem", it)
                    startActivity(intent)
                }
            }
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