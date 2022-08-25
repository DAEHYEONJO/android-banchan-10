package com.woowahan.android10.deliverbanchan.presentation.order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemOrderListBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderListItem

class OrderListAdapter(
    private val itemClick: (orderList: List<UiCartOrderDishJoinItem>) -> Unit
) : ListAdapter<UiOrderListItem, OrderListAdapter.OrderListViewHolder>(
    diffUtil
) {
    companion object {
        const val TAG = "OrderListAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiOrderListItem>() {
            override fun areItemsTheSame(
                oldItem: UiOrderListItem,
                newItem: UiOrderListItem
            ): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

            override fun areContentsTheSame(
                oldItem: UiOrderListItem,
                newItem: UiOrderListItem
            ): Boolean {
                return newItem == oldItem
            }
        }
    }

    inner class OrderListViewHolder(val binding: ItemOrderListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderList: List<UiCartOrderDishJoinItem>, curDeliveryTotalPrice: Int, itemClick: (orderList: List<UiCartOrderDishJoinItem>) -> Unit) {
            with(binding){
                listSize = orderList.size
                item = orderList[0]
                orderTotalPrice = curDeliveryTotalPrice
                root.setOnClickListener {
                    itemClick(orderList)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        val binding =
            ItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.bind(currentList[position].orderList, currentList[position].curDeliveryTotalPrice, itemClick)
    }
}