package com.woowahan.android10.deliverbanchan.presentation.cart.recent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemRecentViewedBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiRecentJoinItem
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecentPagingAdapter @Inject constructor(): PagingDataAdapter<UiRecentJoinItem, RecentPagingAdapter.ViewHolder>(
    diffUtil
) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<UiRecentJoinItem>(){
            override fun areItemsTheSame(oldItem: UiRecentJoinItem, newItem: UiRecentJoinItem): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(oldItem: UiRecentJoinItem, newItem: UiRecentJoinItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(val binding: ItemRecentViewedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(uiRecentJoinItem: UiRecentJoinItem){
            binding.item = uiRecentJoinItem
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentViewedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}