package com.woowahan.android10.deliverbanchan.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemDetailSectionImageBinding

class DetailSectionImageAdapter :
    ListAdapter<String, DetailSectionImageAdapter.DetailSectionViewHolder>(DetailSectionDiffUtil) {

    inner class DetailSectionViewHolder(private val binding: ItemDetailSectionImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String, position: Int) {
            binding.imageUrl = imageUrl
            binding.detailViewBottom.isVisible = (position == currentList.size - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailSectionViewHolder {
        val binding =
            ItemDetailSectionImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DetailSectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailSectionViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object DetailSectionDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }

}