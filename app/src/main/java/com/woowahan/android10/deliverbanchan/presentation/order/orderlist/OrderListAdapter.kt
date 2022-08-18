package com.woowahan.android10.deliverbanchan.presentation.order.orderlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.data.local.model.join.Order
import com.woowahan.android10.deliverbanchan.databinding.ItemOrderListBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderListItem

class OrderListAdapter : ListAdapter<UiOrderListItem, OrderListAdapter.OrderListViewHolder>(
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
        fun bind(orderList: List<Order>) {
            binding.listSize = orderList.size
            binding.order = orderList[0]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        val binding =
            ItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.bind(currentList[position].orderList)
    }
}