package com.woowahan.android10.deliverbanchan.presentation.cart.recently

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemRecentlyViewedBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiRecentlyJoinItem
import com.woowahan.android10.deliverbanchan.presentation.common.dpToPx
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecentlyViewedVerticalAdapter @Inject constructor(): ListAdapter<UiRecentlyJoinItem, RecentlyViewedVerticalAdapter.ViewHolder>(
    diffUtil) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<UiRecentlyJoinItem>(){
            override fun areItemsTheSame(
                oldItem: UiRecentlyJoinItem,
                newItem: UiRecentlyJoinItem
            ): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(
                oldItem: UiRecentlyJoinItem,
                newItem: UiRecentlyJoinItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val binding: ItemRecentlyViewedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(uiRecentlyJoinItem: UiRecentlyJoinItem){
            with(binding){
                itemRecentlyViewedRoot.layoutParams.apply {
                    width = dpToPx(root.context, 120).toInt()
                }
                item = uiRecentlyJoinItem
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentlyViewedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}