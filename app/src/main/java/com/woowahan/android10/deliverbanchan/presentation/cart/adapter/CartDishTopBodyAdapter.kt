package com.woowahan.android10.deliverbanchan.presentation.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartDishTopBodyBinding
import com.woowahan.android10.deliverbanchan.databinding.ItemCartSelectHeaderBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartJoinItem
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartDishTopBodyAdapter @Inject constructor(
): ListAdapter<UiCartJoinItem, CartDishTopBodyAdapter.ViewHolder>(diffUtil) {

    interface OnCartItemClickListener{
        fun onClickDeleteBtn(hash: String)
    }

    var onClickItemClickListener: OnCartItemClickListener? = null

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

    inner class ViewHolder(val binding: ItemCartDishTopBodyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(uiCartJoinItem: UiCartJoinItem){
            with(binding){
                item = uiCartJoinItem
                executePendingBindings()
                cartSelectTopBodyIbDelete.setOnClickListener {
                    onClickItemClickListener?.onClickDeleteBtn(uiCartJoinItem.hash)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartDishTopBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}