package com.woowahan.android10.deliverbanchan.presentation.cart.recent.adapter

import android.util.Log
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

    interface OnRecentItemClickListener: OnDishItemClickListener{
        fun onClickDish(uiDishItem: UiDishItem, position: Int)
    }
    var onDishItemClickListener: OnDishItemClickListener? = null


    companion object{
        const val TAG = "RecentPagingAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiDishItem>(){
            override fun areItemsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(val binding: ItemRecentViewedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(uiDishItem: UiDishItem){
            with(binding){
                Log.e(TAG, "RecentPagingAdapter: $adapterPosition", )
                item = uiDishItem
                itemRecentViewedRoot.setOnClickListener {
                    onDishItemClickListener?.onClickDish(uiDishItem)
                }
                itemRecentViewedIbCart.setOnClickListener {

                    getItem(adapterPosition)?.isInserted = true
                    onDishItemClickListener?.onClickCartIcon(uiDishItem)
                }
                executePendingBindings()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder: pos: $position", )
        getItem(position)?.let {
            Log.e(TAG, "onBindViewHolder: pos: $position $it", )
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentViewedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}