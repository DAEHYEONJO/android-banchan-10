package com.woowahan.android10.deliverbanchan.presentation.maindish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemMaindishLinearBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem

class MainDishLinearAdapter :
    ListAdapter<UiDishItem, MainDishLinearAdapter.MainDishLinearViewHolder>(UiDishItemDiffUtil) {

    inner class MainDishLinearViewHolder(val binding: ItemMaindishLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiDishItem: UiDishItem) {
            binding.uiDishItem = uiDishItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainDishLinearViewHolder {
        val binding =
            ItemMaindishLinearBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MainDishLinearViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainDishLinearViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object UiDishItemDiffUtil : DiffUtil.ItemCallback<UiDishItem>() {
        override fun areItemsTheSame(oldItem: UiDishItem, newItem: UiDishItem) =
            oldItem.hash == newItem.hash

        override fun areContentsTheSame(oldItem: UiDishItem, newItem: UiDishItem) =
            oldItem == newItem
    }
}