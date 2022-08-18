package com.woowahan.android10.deliverbanchan.presentation.order.orderlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.data.local.model.join.Order
import com.woowahan.android10.deliverbanchan.databinding.ItemOrderListBinding

class OrderListAdapter : ListAdapter<Pair<Long, List<Order>>, OrderListAdapter.OrderListViewHolder>(
    diffUtil
) {
    companion object {
        const val TAG = "OrderListAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<Pair<Long, List<Order>>>() {
            override fun areItemsTheSame(
                oldItem: Pair<Long, List<Order>>,
                newItem: Pair<Long, List<Order>>
            ): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(
                oldItem: Pair<Long, List<Order>>,
                newItem: Pair<Long, List<Order>>
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
        holder.bind(currentList[position].second)
    }
}