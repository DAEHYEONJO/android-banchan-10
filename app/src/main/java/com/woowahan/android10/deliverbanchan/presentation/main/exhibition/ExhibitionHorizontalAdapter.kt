package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemExhibitionHorizontalBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem

class ExhibitionHorizontalAdapter(
    private val cartIconClick: (uiDishItem: UiDishItem) -> Unit,
    private val itemClick: (uiDishItem: UiDishItem) -> Unit
) : ListAdapter<UiDishItem, ExhibitionHorizontalAdapter.ViewHolder>(diffUtil) {

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

    inner class ViewHolder(private val binding: ItemExhibitionHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            uiDishItem: UiDishItem, position: Int, cartIconClick: (uiDishItem: UiDishItem) -> Unit,
            itemClick: (uiDishItem: UiDishItem) -> Unit
        ) {
            binding.item = uiDishItem
            binding.viewLeft.isVisible = (position == 0)
            binding.viewRight.isVisible = (position == currentList.size - 1)
            binding.maindishImbCart.setOnClickListener {
                cartIconClick.invoke(uiDishItem)
            }
            binding.root.setOnClickListener {
             itemClick.invoke(uiDishItem)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemExhibitionHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position, cartIconClick, itemClick)
    }
}