package com.woowahan.android10.deliverbanchan.presentation.main.maindish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemMaindishLinearBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem

class MainDishLinearAdapter(
    private val cartIconClick: (uiDishItem: UiDishItem) -> Unit,
    private val itemClick: (uiDishItem: UiDishItem) -> Unit
) :
    ListAdapter<UiDishItem, MainDishLinearAdapter.MainDishLinearViewHolder>(UiDishItemDiffUtil) {

    inner class MainDishLinearViewHolder(val binding: ItemMaindishLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            uiDishItem: UiDishItem,
            cartIconClick: (uiDishItem: UiDishItem) -> Unit,
            isLast: Boolean,
            itemClick: (uiDishItem: UiDishItem) -> Unit
        ) {
            binding.uiDishItem = uiDishItem
            binding.maindishViewFooter.isVisible = isLast
            binding.maindishIbCart.setOnClickListener {
                cartIconClick(uiDishItem)
            }
            binding.root.setOnClickListener {
                itemClick(uiDishItem)
            }
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
        holder.bind(getItem(position), cartIconClick, position == currentList.size - 1, itemClick)
    }

    companion object UiDishItemDiffUtil : DiffUtil.ItemCallback<UiDishItem>() {
        override fun areItemsTheSame(oldItem: UiDishItem, newItem: UiDishItem) =
            oldItem.hash == newItem.hash

        override fun areContentsTheSame(oldItem: UiDishItem, newItem: UiDishItem) =
            oldItem == newItem
    }
}