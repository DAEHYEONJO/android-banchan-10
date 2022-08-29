package com.woowahan.android10.deliverbanchan.presentation.cart.recent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemRecentViewedBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.listeners.OnDishItemClickListener
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@ActivityRetainedScoped
class RecentPagingAdapter @Inject constructor() :
    PagingDataAdapter<UiDishItem, RecentPagingAdapter.ViewHolder>(
        diffUtil
    ) {

    var onDishItemClickListener: OnDishItemClickListener? = null


    companion object {
        const val TAG = "RecentPagingAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiDishItem>() {
            override fun areItemsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(oldItem: UiDishItem, newItem: UiDishItem): Any? {
                return if (oldItem.isInserted != newItem.isInserted || oldItem.timeStamp!=newItem.timeStamp) true else null
            }
        }
    }

    inner class ViewHolder(
        val binding: ItemRecentViewedBinding,
        private val coroutineScope: CoroutineScope
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(uiDishItem: UiDishItem) {
            with(binding) {
                item = uiDishItem
                itemRecentViewedRoot.setClickEventWithDuration(coroutineScope) {
                    onDishItemClickListener?.onClickDish(uiDishItem)
                }
                itemRecentViewedIbCart.setClickEventWithDuration(coroutineScope) {
                    onDishItemClickListener?.onClickCartIcon(uiDishItem)
                }
                executePendingBindings()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            if (payloads[0]==true){
                getItem(position)?.let {
                    holder.bind(it)
                }

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemRecentViewedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.findViewTreeLifecycleOwner()!!.lifecycleScope)
    }

}