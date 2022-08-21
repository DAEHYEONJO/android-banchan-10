package com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartRecentViewedFooterBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.cart.common.GridItemDecorator
import com.woowahan.android10.deliverbanchan.presentation.cart.recent.adapter.RecentViewedVerticalAdapter
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartRecentViewedFooterAdapter @Inject constructor(): RecyclerView.Adapter<CartRecentViewedFooterAdapter.ViewHolder>() {
    var uiRecentJoinList: List<UiDishItem> = emptyList()

    companion object{
        const val TAG = "CartRecentViewedFooterAdapter"
    }

    interface OnCartFooterItemClickListener{
        fun onClickShowAllBtn()
    }
    var onCartFooterItemClickListener: OnCartFooterItemClickListener? = null

    inner class ViewHolder(val binding: ItemCartRecentViewedFooterBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(list: List<UiDishItem>){
            binding.cartRecentViewedFooterTvRecentlyShowAll.setOnClickListener {
                onCartFooterItemClickListener?.onClickShowAllBtn()
            }
            with(binding.cartRecentViewedFooterRv){
                layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = RecentViewedVerticalAdapter().apply {
                    if(itemDecorationCount == 0) addItemDecoration(GridItemDecorator(context))
                    submitList(list)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartRecentViewedFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(uiRecentJoinList)
    }

    override fun getItemCount(): Int = 1


}