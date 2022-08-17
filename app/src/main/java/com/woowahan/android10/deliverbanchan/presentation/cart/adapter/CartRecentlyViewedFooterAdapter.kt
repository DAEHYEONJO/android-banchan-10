package com.woowahan.android10.deliverbanchan.presentation.cart.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartRecentlyViewedFooterBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiRecentlyJoinItem
import com.woowahan.android10.deliverbanchan.presentation.cart.common.GridItemDecorator
import com.woowahan.android10.deliverbanchan.presentation.cart.recently.RecentlyViewedVerticalAdapter
import com.woowahan.android10.deliverbanchan.presentation.common.dpToPx
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
import kotlin.math.roundToInt

@ActivityRetainedScoped
class CartRecentlyViewedFooterAdapter @Inject constructor(): RecyclerView.Adapter<CartRecentlyViewedFooterAdapter.ViewHolder>() {
    var uiRecentlyJoinList: List<UiRecentlyJoinItem> = emptyList()

    companion object{
        const val TAG = "CartRecentlyViewedFooterAdapter"
    }

    class ViewHolder(val binding: ItemCartRecentlyViewedFooterBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(list: List<UiRecentlyJoinItem>){
            with(binding.cartRecentlyViewedFooterRv){
                layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = RecentlyViewedVerticalAdapter().apply {
                    if(itemDecorationCount == 0) addItemDecoration(GridItemDecorator(context))
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