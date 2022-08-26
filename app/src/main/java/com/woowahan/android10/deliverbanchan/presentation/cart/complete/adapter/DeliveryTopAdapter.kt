package com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartOrderCompleteTopBinding
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartCompleteHeader
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class DeliveryTopAdapter @Inject constructor() :
    RecyclerView.Adapter<DeliveryTopAdapter.ViewHolder>() {

    var cartDeliveryTopList = listOf<UiCartCompleteHeader>()

    class ViewHolder(private val binding: ItemCartOrderCompleteTopBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiCartCompleteTop: UiCartCompleteHeader) {
            binding.item = uiCartCompleteTop
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartOrderCompleteTopBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartDeliveryTopList[position])
    }

    override fun getItemCount(): Int = cartDeliveryTopList.size
}