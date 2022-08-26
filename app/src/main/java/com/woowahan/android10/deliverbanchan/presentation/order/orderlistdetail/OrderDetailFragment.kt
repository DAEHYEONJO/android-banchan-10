package com.woowahan.android10.deliverbanchan.presentation.order.orderlistdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentOrderDetailBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.DeliveryBodyAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.DeliveryFooterAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.DeliveryTopAdapter
import com.woowahan.android10.deliverbanchan.presentation.order.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>(
    R.layout.fragment_order_detail, "OrderDetailFragment"
) {

    private val orderViewModel: OrderViewModel by activityViewModels()

    @Inject
    lateinit var orderDetailTopAdapter: DeliveryTopAdapter

    @Inject
    lateinit var orderDetailBodyAdapter: DeliveryBodyAdapter

    @Inject
    lateinit var orderDetailFooterAdapter: DeliveryFooterAdapter
    private val concatAdapter: ConcatAdapter by lazy {
        ConcatAdapter(orderDetailTopAdapter, orderDetailBodyAdapter, orderDetailFooterAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        observeData()
    }

    private fun setRecyclerView() {
        binding.orderDetailRv.apply {
            adapter = concatAdapter
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            with(orderViewModel) {

                reloadBtnClicked.observe(viewLifecycleOwner) {
                    orderDetailTopAdapter.notifyDataSetChanged()
                }

                selectedOrderHeader.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
                    orderDetailTopAdapter.cartDeliveryTopList = listOf(it)
                    orderDetailTopAdapter.notifyDataSetChanged()
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                selectedOrderList.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
                    orderDetailBodyAdapter.cartDeliveryTopList = it
                    orderDetailBodyAdapter.notifyDataSetChanged()
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                selectedOrderInfo.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
                    orderDetailFooterAdapter.cartDeliveryBottomList = listOf(it)
                    orderDetailFooterAdapter.notifyDataSetChanged()
                }.launchIn(viewLifecycleOwner.lifecycleScope)

            }
        }
    }
}