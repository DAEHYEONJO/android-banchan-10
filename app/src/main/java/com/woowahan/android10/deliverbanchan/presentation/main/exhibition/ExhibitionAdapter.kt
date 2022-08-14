package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemExhibitionBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem

class ExhibitionAdapter :
    ListAdapter<UiExhibitionItem, ExhibitionAdapter.ExhibitionViewHolder>(diffUtil) {

    companion object {
        const val TAG = "ExhibitionAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiExhibitionItem>() {
            override fun areItemsTheSame(oldItem: UiExhibitionItem, newItem: UiExhibitionItem): Boolean {
                return oldItem.categoryId == newItem.categoryId
            }

            override fun areContentsTheSame(oldItem: UiExhibitionItem, newItem: UiExhibitionItem): Boolean {
                return newItem == oldItem
            }
        }
    }

    inner class ExhibitionViewHolder(private val binding: ItemExhibitionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiExhibitionItem: UiExhibitionItem) {
            binding.uiExhibitionItem = uiExhibitionItem
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionViewHolder {
        val binding = ItemExhibitionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExhibitionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExhibitionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}