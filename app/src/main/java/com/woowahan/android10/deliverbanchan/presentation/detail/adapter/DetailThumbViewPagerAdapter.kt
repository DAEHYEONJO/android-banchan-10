package com.woowahan.android10.deliverbanchan.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemDetailThumbImageBinding
import com.woowahan.android10.deliverbanchan.databinding.ItemDetailThumbImageRvBinding

class DetailThumbViewPagerAdapter :
    ListAdapter<String, DetailThumbViewPagerAdapter.DetailThumbViewPagerHolder>(
        DetailThumbViewPagerDiffUtil
    ) {

    inner class DetailThumbViewPagerHolder(private val binding: ItemDetailThumbImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            binding.imageUrl = imgUrl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailThumbViewPagerHolder {
        val binding =
            ItemDetailThumbImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DetailThumbViewPagerHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailThumbViewPagerHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = currentList.size

    companion object DetailThumbViewPagerDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}