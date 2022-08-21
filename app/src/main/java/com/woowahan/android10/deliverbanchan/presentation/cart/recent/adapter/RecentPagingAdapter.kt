package com.woowahan.android10.deliverbanchan.presentation.cart.recent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemRecentViewedBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.common.OnDishItemClickListener
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecentPagingAdapter @Inject constructor(): PagingDataAdapter<UiDishItem, RecentPagingAdapter.ViewHolder>(
    diffUtil
) {

    var onDishItemClickListener: OnDishItemClickListener? = null

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<UiDishItem>(){
            override fun areItemsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(val binding: ItemRecentViewedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(uiDishItem: UiDishItem){
            with(binding){
                item = uiDishItem
                itemRecentViewedRoot.setOnClickListener {
                    onDishItemClickListener?.onClickDish(uiDishItem)
                }
                itemRecentViewedIbCart.setOnClickListener {
                    onDishItemClickListener?.onClickCartIcon(uiDishItem)
                }
                executePendingBindings()
            }
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