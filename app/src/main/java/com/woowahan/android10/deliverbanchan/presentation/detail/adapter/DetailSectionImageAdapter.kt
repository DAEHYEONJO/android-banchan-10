package com.woowahan.android10.deliverbanchan.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemDetailSectionImageBinding

class DetailSectionImageAdapter :
    ListAdapter<String, DetailSectionImageAdapter.DetailSectionViewHolder>(DetailSectionDiffUtil) {

    inner class DetailSectionViewHolder(private val binding: ItemDetailSectionImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            binding.imageUrl = imageUrl
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
        holder.bind(getItem(position))
    }

    companion object DetailSectionDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }

}