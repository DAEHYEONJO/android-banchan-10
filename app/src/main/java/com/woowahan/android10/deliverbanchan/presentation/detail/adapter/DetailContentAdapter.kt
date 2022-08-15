package com.woowahan.android10.deliverbanchan.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemDetailContentBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo

class DetailContentAdapter : ListAdapter<UiDetailInfo, DetailContentAdapter.DetailContentViewHolder>(DetailContentDiffUtil) {

    inner class DetailContentViewHolder(private val binding: ItemDetailContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiDetailInfo: UiDetailInfo) {
            binding.uiDetailInfo = uiDetailInfo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailContentViewHolder {
        val binding =
            ItemDetailContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DetailContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailContentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DetailContentDiffUtil : DiffUtil.ItemCallback<UiDetailInfo>() {
        override fun areItemsTheSame(oldItem: UiDetailInfo, newItem: UiDetailInfo) =
            oldItem.hash == newItem.hash

        override fun areContentsTheSame(oldItem: UiDetailInfo, newItem: UiDetailInfo) =
            oldItem == newItem
    }
}