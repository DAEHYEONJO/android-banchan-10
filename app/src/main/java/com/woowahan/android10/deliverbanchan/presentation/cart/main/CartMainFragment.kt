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
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.background.DeliveryReceiver
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartMainBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter.CartDishTopBodyAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter.CartOrderInfoBottomBodyAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter.CartRecentViewedFooterAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter.CartSelectHeaderAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartCompleteHeader.Companion.ESTIMATED_DELIVERY_TIME
import com.woowahan.android10.deliverbanchan.presentation.cart.viewmodel.CartViewModel
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.NumberDialogFragment
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.security.SecureRandom
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
    lateinit var cartRecentViewedFooterAdapter: CartRecentViewedFooterAdapter
    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder()
            .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS).build()
        cartHeaderAdapter.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            if (!hasObservers()) {
                setHasStableIds(true)
            }
        }
        cartTopBodyAdapter.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            if (!hasObservers()) {
                setHasStableIds(true)
            }
        }
        cartBottomBodyAdapter.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            if (!hasObservers()) {
                setHasStableIds(true)
            }
        }
        cartRecentViewedFooterAdapter.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            if (!hasObservers()) {
                setHasStableIds(true)
            }
        }
        ConcatAdapter(
            config,
            cartHeaderAdapter,
            cartTopBodyAdapter,
            cartBottomBodyAdapter,
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

    private fun initInterface() {
        cartBottomBodyAdapter.onCartBottomBodyItemClickListener =
            object : CartOrderInfoBottomBodyAdapter.OnCartBottomBodyItemClickListener {
                override fun onClickOrderBtn() {
                    cartViewModel.setOrderCompleteCartItem()
                }
            }
        cartHeaderAdapter.onCartSelectHeaderItemClickListener =
            object : CartSelectHeaderAdapter.OnCartSelectHeaderItemClickListener {
                override fun onClickDeleteBtn() {
                    cartViewModel.deleteUiCartItemByHash { success ->
                        if (success) requireContext().showToast("삭제에 성공했습니다.")
                    }
                }

                override fun onClickSelectedToggleBtn(checkedState: Boolean) {
                    cartViewModel.changeCheckedState(!checkedState)
                }
            }
        cartTopBodyAdapter.onCartTopBodyItemClickListener =
            object : CartDishTopBodyAdapter.OnCartTopBodyItemClickListener {
                override fun onClickDeleteBtn(position: Int, hash: String) {
                    cartViewModel.deleteUiCartItemByPos(position, hash)
                }

                override fun onCheckBoxCheckedChanged(
                    position: Int,
                    hash: String,
                    checked: Boolean
                ) {
                    cartViewModel.updateUiCartCheckedValue(position, !checked)
                    //binding.cartMainRv.scrollToPosition(binding.cartMainRv.)
                }

                override fun onClickAmountBtn(position: Int, hash: String, amount: Int) {
                    cartViewModel.updateUiCartAmountValue(position, amount)
                }

                override fun onClickAmountTv(position: Int, amount: Int) {
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


            itemCartHeaderData.observe(viewLifecycleOwner) { uiCartHeader ->
                lifecycleScope.launch {
                    cartHeaderAdapter.submitList(listOf(uiCartHeader))
                }
            }

            itemCartBottomBodyData.observe(viewLifecycleOwner) { uiCartBottomBody ->
                with(cartBottomBodyAdapter) {
                    submitList(listOf(uiCartBottomBody))
                }
            }

            uiCartJoinList.observe(viewLifecycleOwner) {
                val newList = ArrayList<UiCartOrderDishJoinItem>().apply {
                    it.forEach {
                        add(it.copy())
                    }
                }
                cartTopBodyAdapter.submitList(newList)
                cartViewModel.calcCartBottomBodyAndHeaderVal(it)
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

    private fun <A, T> handleState(adapter: A, uiState: UiState<T>) {
        when (uiState) {
            is UiState.Init -> {}
            is UiState.Empty -> {}
            is UiState.Loading -> {}
            is UiState.Success -> {
                when (adapter) {
                    is CartDishTopBodyAdapter -> {
                        adapter.submitList(uiState.items as List<UiCartOrderDishJoinItem>)
                    }
                    is CartRecentViewedFooterAdapter -> {
                        with(adapter) {
                            recentOnDishItemClickListener = this@CartMainFragment
                            submitList(listOf(uiState.items as List<UiDishItem>))
                        }
                    }
                }
            }
            is UiState.Error -> {}
        }
    }

    override fun onStop() {
        super.onStop()
    }

    private fun initRecyclerView() {
        with(binding.cartMainRv) {
            adapter = concatAdapter
            itemAnimator = null
        }
    }

}