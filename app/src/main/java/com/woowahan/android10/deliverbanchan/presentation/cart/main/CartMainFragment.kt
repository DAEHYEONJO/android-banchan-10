package com.woowahan.android10.deliverbanchan.presentation.cart.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartMainBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiRecentlyJoinItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.CartViewModel
import com.woowahan.android10.deliverbanchan.presentation.cart.adapter.CartDishTopBodyAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.adapter.CartOrderInfoBottomBodyAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.adapter.CartRecentlyViewedFooterAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.adapter.CartSelectHeaderAdapter
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CartMainFragment : BaseFragment<FragmentCartMainBinding>(
    R.layout.fragment_cart_main, "CartMainFragment"
) {

    @Inject
    lateinit var cartHeaderAdapter: CartSelectHeaderAdapter
    @Inject
    lateinit var cartTopBodyAdapter: CartDishTopBodyAdapter
    @Inject
    lateinit var cartBottomBodyAdapter: CartOrderInfoBottomBodyAdapter
    @Inject
    lateinit var cartRecentlyViewedFooterAdapter: CartRecentlyViewedFooterAdapter
    private val concatAdapter: ConcatAdapter by lazy {
        ConcatAdapter(
            cartHeaderAdapter,
            cartTopBodyAdapter,
            cartBottomBodyAdapter,
            cartRecentlyViewedFooterAdapter
        )
    }
    private val cartViewModel: CartViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInterface()
        initAdapterList()
        initRecyclerView()
    }

    private fun initInterface() {
        cartHeaderAdapter.onCartTopBodyItemClickListener =
            object : CartSelectHeaderAdapter.OnCartTopBodyItemClickListener{
                override fun onClickSelectedDelete() {
                    cartViewModel.deleteUiCartItemByHash { success ->
                        if (success) requireContext().showToast("삭제에 성공했습니다.")
                        else requireContext().showToast("삭제에 실패했습니다.")
                    }
                }

                override fun onClickSelectedStateChange(checkedState: Boolean) {
                    cartViewModel.changeCheckedState(!checkedState)
                }
            }
        cartTopBodyAdapter.onClickItemClickListener =
            object : CartDishTopBodyAdapter.OnCartTopBodyItemClickListener {
                override fun onClickDeleteBtn(position: Int, hash: String) {
                    cartViewModel.deleteUiCartItemByPos(position, hash)
                    cartTopBodyAdapter.notifyItemChanged(position)
                    //cartTopBodyAdapter.notifyDataSetChanged()
                }

                override fun onCheckBoxCheckedChanged(position: Int, hash: String, checked: Boolean) {
                    cartViewModel.updateUiCartCheckedValue(position, !checked)
                    cartTopBodyAdapter.notifyItemChanged(position)
                    //cartTopBodyAdapter.notifyDataSetChanged()
                }

                override fun onClickAmountBtn(position: Int, hash: String, amount: Int) {
                    cartViewModel.updateUiCartAmountValue(position, amount)
                    cartTopBodyAdapter.notifyItemChanged(position)
                    //cartTopBodyAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun initAdapterList() {
        with(cartViewModel) {
            allCartJoinState.flowWithLifecycle(lifecycle).onEach { uiLocalState ->
                handleState(cartTopBodyAdapter, uiLocalState)
            }.launchIn(lifecycleScope)

            allRecentlyJoinState.flowWithLifecycle(lifecycle).onEach { uiLocalState ->
                handleState(cartRecentlyViewedFooterAdapter, uiLocalState)
            }.launchIn(lifecycleScope)

            itemCartHeaderData.observe(viewLifecycleOwner) { uiCartHeader ->
                with(cartHeaderAdapter) {
                    selectHeaderList = listOf(uiCartHeader)
                    notifyDataSetChanged()
                }
            }
            itemCartBottomBodyData.observe(viewLifecycleOwner){ uiCartBottomBody ->
                with(cartBottomBodyAdapter){
                    bottomBodyList = listOf(uiCartBottomBody)
                    notifyDataSetChanged()
                }
            }
            uiCartJoinList.observe(viewLifecycleOwner){
                cartTopBodyAdapter.submitList(it.toList())
                cartViewModel.calcCartBottomBodyAndHeaderVal(it)
            }
        }
    }

    private fun <A, T> handleState(adapter: A, uiLocalState: UiLocalState<T>) {
        when (uiLocalState) {
            is UiLocalState.IsEmpty -> {}
            is UiLocalState.IsLoading -> {}
            is UiLocalState.ShowToast -> {
                requireContext().showToast(uiLocalState.message)
            }
            is UiLocalState.Success -> {
               when (adapter) {
                    is CartDishTopBodyAdapter -> {
                        adapter.submitList(uiLocalState.uiDishItems as List<UiCartJoinItem>)
                    }
                    is CartRecentlyViewedFooterAdapter -> {
                        with(adapter) {
                            uiRecentlyJoinList = uiLocalState.uiDishItems as List<UiRecentlyJoinItem>
                            notifyDataSetChanged()
                        }
                    }
                }
            }
            is UiLocalState.Error -> {}
        }
    }

    private fun initRecyclerView() {
        with(binding.cartMainRv) {
            adapter = concatAdapter
        }
    }

    override fun onStop() {
        super.onStop()
        //cartViewModel.updateAllCartItemChanged()
    }

}