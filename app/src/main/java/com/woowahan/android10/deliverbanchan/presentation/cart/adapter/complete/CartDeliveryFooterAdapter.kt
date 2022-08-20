package com.woowahan.android10.deliverbanchan.presentation.cart.adapter.complete

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartOrderCompleteFooterBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderInfo
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartDeliveryFooterAdapter @Inject constructor(): RecyclerView.Adapter<CartDeliveryFooterAdapter.ViewHolder>() {

    var cartDeliveryBottomList = listOf<UiOrderInfo>()

    class ViewHolder(private val binding: ItemCartOrderCompleteFooterBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(uiOrderInfo: UiOrderInfo){
            binding.item = uiOrderInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartOrderCompleteFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartDeliveryBottomList[position])
    }

    override fun getItemCount(): Int = cartDeliveryBottomList.size
}