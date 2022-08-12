package com.woowahan.android10.deliverbanchan.presentation.main.maindish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemMaindishGridBinding
import com.woowahan.android10.deliverbanchan.databinding.ItemSoupBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem


class MainDishGridAdapter : ListAdapter<UiDishItem, MainDishGridAdapter.ViewHolder>(diffUtil) {

    companion object {
        const val TAG = "MainDishGridAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiDishItem>() {
            override fun areItemsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return newItem == oldItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemMaindishGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiDishItem: UiDishItem, position: Int) {
            binding.item = uiDishItem
            binding.viewLeft.isVisible = (position % 2 == 0)
            binding.viewRight.isVisible = (position % 2 != 0)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMaindishGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}