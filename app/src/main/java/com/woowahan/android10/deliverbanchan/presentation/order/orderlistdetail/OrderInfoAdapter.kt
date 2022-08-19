package com.woowahan.android10.deliverbanchan.presentation.order.orderlistdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemOrderInfoBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderInfo

class OrderInfoAdapter : ListAdapter<UiOrderInfo, OrderInfoAdapter.OrderInfoViewHolder>(diffUtil) {
    companion object {
        const val TAG = "OrderInfoAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiOrderInfo>() {
            override fun areItemsTheSame(
                oldItem: UiOrderInfo,
                newItem: UiOrderInfo
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UiOrderInfo,
                newItem: UiOrderInfo
            ): Boolean {
                return newItem == oldItem
            }
        }
    }

    inner class OrderInfoViewHolder(val binding: ItemOrderInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiOrderInfo: UiOrderInfo) {
            binding.uiOrderInfo = uiOrderInfo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderInfoViewHolder {
        val binding =
            ItemOrderInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderInfoViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}