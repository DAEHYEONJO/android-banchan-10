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
import com.woowahan.android10.deliverbanchan.presentation.common.showToast
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
        cartHeaderAdapter.onClick = {
            with(cartViewModel) {
                if (allCartJoinState.value is UiLocalState.Success<UiCartJoinItem>) {
                    changeAllCartCheckedState(it)
                } else {
                    Log.e(TAG, "initInterface: not success state")
                }
            }
        }
        cartTopBodyAdapter.onClickItemClickListener =
            object : CartDishTopBodyAdapter.OnCartItemClickListener {
                override fun onClickDeleteBtn(hash: String) {
                    cartViewModel.deleteCart(hash)
                }

                override fun onCheckBoxCheckedChanged(hash: String, checked: Boolean) {
                    cartViewModel.updateCartCheckedValue(hash, checked)
                }

                override fun onClickAmountBtn(hash: String, amount: Int) {
                    cartViewModel.updateCartAmountValue(hash, amount)
                }
            }
    }

    private fun initAdapterList() {
        with(cartViewModel) {
            allCartJoinState.flowWithLifecycle(lifecycle).onEach { uiLocalState ->
                Log.e(TAG, "initAdapterList: cart join $uiLocalState")
                handleState(cartTopBodyAdapter, uiLocalState)
            }.launchIn(lifecycleScope)

            allRecentlyJoinState.flowWithLifecycle(lifecycle).onEach { uiLocalState ->
                Log.e(TAG, "initAdapterList: recently join $uiLocalState")
                handleState(cartRecentlyViewedFooterAdapter, uiLocalState)
            }.launchIn(lifecycleScope)

            itemCartHeaderChecked.observe(viewLifecycleOwner) {
                with(cartHeaderAdapter) {
                    selectHeaderList[0] = it
                    notifyItemChanged(0)
                }
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
                            uiRecentlyJoinList =
                                uiLocalState.uiDishItems as List<UiRecentlyJoinItem>
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

}