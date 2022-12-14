package com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartOrderCompleteBodyBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.presentation.cart.common.LinearItemDecorator
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class DeliveryBodyAdapter @Inject constructor() :
    RecyclerView.Adapter<DeliveryBodyAdapter.ViewHolder>() {

    var cartDeliveryTopList = emptyList<UiCartOrderDishJoinItem>()

    inner class ViewHolder(private val binding: ItemCartOrderCompleteBodyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiCartJoinList: List<UiCartOrderDishJoinItem>) {
            with(binding.cartOrderCompleteBodyRv) {
                adapter = DeliveryBottomItemAdapter().apply {
                    if (itemDecorationCount == 0) addItemDecoration(LinearItemDecorator(context))
                    submitList(uiCartJoinList)
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemCartOrderCompleteBodyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartDeliveryTopList)
    }

    override fun getItemCount(): Int = 1
}