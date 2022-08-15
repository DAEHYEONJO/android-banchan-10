package com.woowahan.android10.deliverbanchan.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemDetailThumbImageRvBinding

class DetailThumbImageAdapter :
    ListAdapter<List<String>, DetailThumbImageAdapter.DetailThumbImageHolder>(
        DetailThumbImageDiffUtil
    ) {

    inner class DetailThumbImageHolder(private val binding: ItemDetailThumbImageRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrlList: List<String>) {
            val vpAdapter = DetailThumbViewPagerAdapter()
            binding.detailVpThumb.apply {
                adapter = vpAdapter
            }
            vpAdapter.submitList(imgUrlList.toList())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailThumbImageHolder {
        val binding =
            ItemDetailThumbImageRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DetailThumbImageHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailThumbImageHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DetailThumbImageDiffUtil : DiffUtil.ItemCallback<List<String>>() {
        override fun areItemsTheSame(oldItem: List<String>, newItem: List<String>) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: List<String>, newItem: List<String>) =
            oldItem == newItem
    }
}