package com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartOrderCompleteBodyItemBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class DeliveryBottomItemAdapter @Inject constructor()
    : ListAdapter<UiCartOrderDishJoinItem, DeliveryBottomItemAdapter.ViewHolder>(diffUtil) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<UiCartOrderDishJoinItem>(){
            override fun areItemsTheSame(
                oldItem: UiCartOrderDishJoinItem,
                newItem: UiCartOrderDishJoinItem
            ): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(
                oldItem: UiCartOrderDishJoinItem,
                newItem: UiCartOrderDishJoinItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(val binding: ItemCartOrderCompleteBodyItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(uiCartOrderDishJoinItem: UiCartOrderDishJoinItem){
            binding.item = uiCartOrderDishJoinItem
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartOrderCompleteBodyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}