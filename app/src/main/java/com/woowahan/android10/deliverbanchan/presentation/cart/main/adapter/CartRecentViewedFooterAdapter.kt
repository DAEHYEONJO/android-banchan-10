package com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ItemCartRecentViewedFooterBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.listeners.OnDishItemClickListener
import com.woowahan.android10.deliverbanchan.presentation.cart.common.GridItemDecorator
import com.woowahan.android10.deliverbanchan.presentation.cart.recent.adapter.RecentViewedVerticalAdapter
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartRecentViewedFooterAdapter @Inject constructor() :
    ListAdapter<List<UiDishItem>, CartRecentViewedFooterAdapter.ViewHolder>(itemCallback) {

    companion object {
        val itemCallback = object : DiffUtil.ItemCallback<List<UiDishItem>>() {
            override fun areItemsTheSame(
                oldItem: List<UiDishItem>,
                newItem: List<UiDishItem>
            ): Boolean {
                return oldItem.size == newItem.size
            }

            override fun areContentsTheSame(
                oldItem: List<UiDishItem>,
                newItem: List<UiDishItem>
            ): Boolean {
                return oldItem == newItem
            }
        }
        const val TAG = "CartRecentViewedFooterAdapter"
    }

    interface OnCartFooterItemClickListener {
        fun onClickShowAllBtn()
    }

    var onCartFooterItemClickListener: OnCartFooterItemClickListener? = null
    var recentOnDishItemClickListener: OnDishItemClickListener? = null

    inner class ViewHolder(val binding: ItemCartRecentViewedFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: List<UiDishItem>) {
            binding.cartRecentViewedFooterTvRecentlyShowAll.setOnClickListener {
                onCartFooterItemClickListener?.onClickShowAllBtn()
            }
            with(binding.cartRecentViewedFooterRv) {
                layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = RecentViewedVerticalAdapter().apply {
                    this.onDishItemClickListener = recentOnDishItemClickListener
                    if (itemDecorationCount == 0) addItemDecoration(GridItemDecorator(context))
                    submitList(list)
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return R.layout.item_cart_recent_viewed_footer.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartRecentViewedFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}