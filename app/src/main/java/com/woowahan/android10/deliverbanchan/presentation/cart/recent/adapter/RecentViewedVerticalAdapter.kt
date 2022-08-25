package com.woowahan.android10.deliverbanchan.presentation.cart.recent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemRecentViewedBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.listeners.OnDishItemClickListener
import com.woowahan.android10.deliverbanchan.presentation.common.ext.dpToPx
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecentViewedVerticalAdapter @Inject constructor(): ListAdapter<UiDishItem, RecentViewedVerticalAdapter.ViewHolder>(
    diffUtil
) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<UiDishItem>(){
            override fun areItemsTheSame(
                oldItem: UiDishItem,
                newItem: UiDishItem
            ): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(
                oldItem: UiDishItem,
                newItem: UiDishItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    var onDishItemClickListener: OnDishItemClickListener? = null

    inner class ViewHolder(private val binding: ItemRecentViewedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(uiDishItem: UiDishItem){
            with(binding){
                item = uiDishItem
                itemRecentViewedIbCart.toGone()
                binding.itemRecentViewedRoot.setOnClickListener {
                    onDishItemClickListener?.onClickDish(uiDishItem)
                }

                itemRecentViewedRoot.layoutParams.apply {
                    width = dpToPx(root.context, 120).toInt()
                }
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