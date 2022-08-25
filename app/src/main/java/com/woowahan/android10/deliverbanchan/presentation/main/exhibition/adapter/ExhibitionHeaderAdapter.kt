package com.woowahan.android10.deliverbanchan.presentation.main.exhibition.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemExhibitionHeaderBinding

class ExhibitionHeaderAdapter :
    ListAdapter<String, ExhibitionHeaderAdapter.ExhibitionHeaderViewHolder>(ExhibitionHeaderDiffUtil) {

    inner class ExhibitionHeaderViewHolder(private val binding: ItemExhibitionHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(str: String) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionHeaderViewHolder {
        val binding =
            ItemExhibitionHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ExhibitionHeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExhibitionHeaderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object ExhibitionHeaderDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}