package com.woowahan.android10.deliverbanchan.presentation.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartRecentlyViewedFooterBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiRecentlyJoinItem
import com.woowahan.android10.deliverbanchan.presentation.cart.recently.RecentlyViewedVerticalAdapter
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartRecentlyViewedFooterAdapter @Inject constructor(): RecyclerView.Adapter<CartRecentlyViewedFooterAdapter.ViewHolder>() {
    var uiRecentlyJoinList: List<UiRecentlyJoinItem> = emptyList()
    class ViewHolder(val binding: ItemCartRecentlyViewedFooterBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(list: List<UiRecentlyJoinItem>){
            with(binding.cartRecentlyViewedFooterRv){
                layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = RecentlyViewedVerticalAdapter().apply {
                    submitList(list)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartRecentlyViewedFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(uiRecentlyJoinList)
    }

    override fun getItemCount(): Int = 1


}