package com.woowahan.android10.deliverbanchan.presentation.cart.adapter.complete

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartOrderCompleteBodyItemBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartJoinItem
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartDeliveryBottomItemAdapter @Inject constructor()
    : ListAdapter<UiCartJoinItem, CartDeliveryBottomItemAdapter.ViewHolder>(diffUtil) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<UiCartJoinItem>(){
            override fun areItemsTheSame(
                oldItem: UiCartJoinItem,
                newItem: UiCartJoinItem
            ): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(
                oldItem: UiCartJoinItem,
                newItem: UiCartJoinItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(val binding: ItemCartOrderCompleteBodyItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(uiCartJoinItem: UiCartJoinItem){
            binding.item = uiCartJoinItem
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