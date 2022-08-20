package com.woowahan.android10.deliverbanchan.presentation.cart.recent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemRecentViewedBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiRecentJoinItem
import com.woowahan.android10.deliverbanchan.presentation.common.ext.dpToPx
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecentViewedVerticalAdapter @Inject constructor(): ListAdapter<UiRecentJoinItem, RecentViewedVerticalAdapter.ViewHolder>(
    diffUtil) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<UiRecentJoinItem>(){
            override fun areItemsTheSame(
                oldItem: UiRecentJoinItem,
                newItem: UiRecentJoinItem
            ): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(
                oldItem: UiRecentJoinItem,
                newItem: UiRecentJoinItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val binding: ItemRecentViewedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(uiRecentJoinItem: UiRecentJoinItem){
            with(binding){
                itemRecentViewedIbCart.toGone()
                itemRecentViewedRoot.layoutParams.apply {
                    width = dpToPx(root.context, 120).toInt()
                }
                item = uiRecentJoinItem
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentViewedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}