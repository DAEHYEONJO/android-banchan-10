package com.woowahan.android10.deliverbanchan.presentation.order.orderlistdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.data.local.model.join.Order
import com.woowahan.android10.deliverbanchan.databinding.ItemOrderDetailBinding
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible

class OrderDetailAdapter : ListAdapter<Order, OrderDetailAdapter.OrderDetailViewHolder>(diffUtil) {
    companion object {
        const val TAG = "OrderDetailAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(
                oldItem: Order,
                newItem: Order
            ): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(
                oldItem: Order,
                newItem: Order
            ): Boolean {
                return newItem == oldItem
            }
        }
    }

    inner class OrderDetailViewHolder(val binding: ItemOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order, position: Int) {
            binding.order = order
            if (adapterPosition == currentList.size - 1){
                binding.orderDetailViewBottom.toVisible()
            }else{
                binding.orderDetailViewBottom.toGone()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val binding =
            ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }
}