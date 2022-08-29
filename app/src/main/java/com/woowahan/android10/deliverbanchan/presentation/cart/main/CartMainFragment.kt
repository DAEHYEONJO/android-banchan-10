package com.woowahan.android10.deliverbanchan.presentation.cart.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.background.DeliveryReceiver
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartMainBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartCompleteHeader.Companion.ESTIMATED_DELIVERY_TIME
import com.woowahan.android10.deliverbanchan.domain.model.UiCartMultiViewType
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter.CartMultiViewTypeAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter.CartRecentViewedFooterAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.viewmodel.CartViewModel
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.NumberDialogFragment
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.security.SecureRandom
import javax.inject.Inject

@AndroidEntryPoint
class CartMainFragment : BaseFragment<FragmentCartMainBinding>(
    R.layout.fragment_cart_main, "CartMainFragment"
) {
    @Inject
    lateinit var cartRecentViewedFooterAdapter: CartRecentViewedFooterAdapter

    @Inject
    lateinit var cartMultiViewTypeAdapter: CartMultiViewTypeAdapter

    private val concatAdapter: ConcatAdapter by lazy {
        ConcatAdapter(
            cartMultiViewTypeAdapter,
            cartRecentViewedFooterAdapter
        )
    }
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInterface()
        initAdapterList()
        initRecyclerView()
        observeOrderButtonClickEvent()
    }

    private fun initRecyclerView() {
        with(binding.cartMainRv) {
            adapter = concatAdapter
            itemAnimator = null
        }
    }

    private fun initInterface() {
        cartMultiViewTypeAdapter.onCartMultiViewTypeClickInterface =
            object : CartMultiViewTypeAdapter.OnCartMultiViewTypeClickInterface {
                override fun onClickOrderBtn() {
                    cartViewModel.setOrderCompleteCartItem()
                }

                override fun onClickHeaderDeleteBtn() {
                    cartViewModel.deleteUiCartItemByHash()
                }

                override fun onClickHeaderSelectedToggleBtn(checkState: Boolean) {
                    cartViewModel.changeCheckedState(!checkState)
                }

                override fun onClickBodyDeleteBtn(position: Int, hash: String) {
                    cartViewModel.deleteUiCartItemByPos(position, hash)
                }

                override fun onClickBodyCheckBox(position: Int, hash: String, checked: Boolean) {
                    cartViewModel.updateUiCartCheckedValue(position, !checked)
                }

                override fun onClickBodyAmountBtn(position: Int, hash: String, amount: Int) {
                    cartViewModel.updateUiCartAmountValue(position, amount)
                }

                override fun onClickBodyAmountTv(position: Int, amount: Int) {
                    var dialogFragment =
                        parentFragmentManager.findFragmentByTag("NumberDialogFragment")
                    dialogFragment = if (dialogFragment == null) NumberDialogFragment()
                    else dialogFragment as NumberDialogFragment
                    dialogFragment.apply {
                        arguments = Bundle().apply {
                            putInt("position", position)
                            putInt("amount", amount)
                        }
                    }
                    dialogFragment.show(parentFragmentManager, "NumberDialogFragment")
                }
            }

        cartRecentViewedFooterAdapter.onCartFooterItemClickListener =
            object : CartRecentViewedFooterAdapter.OnCartFooterItemClickListener {
                override fun onClickShowAllBtn() {
                    cartViewModel.fragmentArrayIndex.value = 2
                }
            }
    }

    private fun initAdapterList() {
        with(cartViewModel) {

            allRecentSevenJoinState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach { uiTempState ->
                    handleState(cartRecentViewedFooterAdapter, uiTempState)
                }.launchIn(viewLifecycleOwner.lifecycleScope)


            allCartJoinMultiViewTypeState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach { uiState: UiState<List<UiCartMultiViewType>> ->
                    handleState(cartMultiViewTypeAdapter, uiState)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun <A, T> handleState(adapter: A, uiState: UiState<T>) {
        when (uiState) {
            is UiState.Init -> {}
            is UiState.Empty -> {}
            is UiState.Loading -> {
                binding.cartMainRv.toGone()
                binding.cartMainPb.toVisible()
            }
            is UiState.Success -> {
                binding.cartMainPb.toGone()
                binding.cartMainRv.toVisible()
                when (adapter) {
                    is CartMultiViewTypeAdapter -> {
                        val newList = ArrayList<UiCartMultiViewType>().apply {
                            (uiState.items as List<UiCartMultiViewType>).forEach {
                                add(it.copy())
                            }
                        }
                        adapter.submitList(newList)
                    }
                    is CartRecentViewedFooterAdapter -> {
                        with(adapter) {
                            recentOnDishItemClickListener = this@CartMainFragment
                            submitList(listOf(uiState.items as List<UiDishItem>))
                        }
                    }
                }
            }
            is UiState.Error -> {
                binding.cartMainPb.toGone()
                binding.cartMainRv.toGone()
                requireContext().showToast(resources.getString(R.string.error_screen_cannot_load))
            }
        }
    }

    private fun observeOrderButtonClickEvent() {
        with(cartViewModel) {
            orderButtonClicked.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach { btnClicked ->
                    if (btnClicked) {
                        val alarmManager =
                            (requireContext().getSystemService(Context.ALARM_SERVICE)) as AlarmManager
                        val intent = Intent(requireContext(), DeliveryReceiver::class.java).apply {
                            putStringArrayListExtra("orderHashList", cartViewModel.orderHashList)
                            putExtra("firstItemTitle", cartViewModel.orderFirstItemTitle)
                            putExtra("timeStamp", cartViewModel.currentOrderTimeStamp)
                        }

                        val pendingIntent = PendingIntent.getBroadcast(
                            requireContext(),
                            SecureRandom().nextInt(Int.MAX_VALUE),
                            intent,
                            PendingIntent.FLAG_MUTABLE
                        )

                        val triggerTime =
                            (SystemClock.elapsedRealtime() + ESTIMATED_DELIVERY_TIME) // 테스트용 = 현재 10초
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                triggerTime,
                                pendingIntent
                            )
                        } else {
                            alarmManager.set(
                                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                triggerTime,
                                pendingIntent
                            )
                        }
                        cartViewModel.fragmentArrayIndex.value = 1 // fragment 전환 -> 주문 디테일 화면
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }
}