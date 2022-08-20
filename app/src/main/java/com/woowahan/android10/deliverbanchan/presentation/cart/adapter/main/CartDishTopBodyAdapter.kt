package com.woowahan.android10.deliverbanchan.presentation.cart.adapter.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartDishTopBodyBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartJoinItem
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartDishTopBodyAdapter @Inject constructor(
) : ListAdapter<UiCartJoinItem, CartDishTopBodyAdapter.ViewHolder>(diffUtil) {

    interface OnCartTopBodyItemClickListener {
        fun onClickDeleteBtn(position: Int, hash: String)
        fun onCheckBoxCheckedChanged(position: Int, hash: String, checked: Boolean)
        fun onClickAmountBtn(position: Int, hash: String, amount: Int)
    }

    var onClickItemClickListener: OnCartTopBodyItemClickListener? = null

    companion object {
        const val TAG = "CartDishTopBodyAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiCartJoinItem>() {
            override fun areItemsTheSame(
                oldItem: UiCartJoinItem,
                newItem: UiCartJoinItem
            ): Boolean {
                return oldItem.hash == newItem.hash && oldItem.checked == newItem.checked && oldItem.amount == newItem.amount
            }

            override fun areContentsTheSame(
                oldItem: UiCartJoinItem,
                newItem: UiCartJoinItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(val binding: ItemCartDishTopBodyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiCartJoinItem: UiCartJoinItem) {
            with(binding) {
                item = uiCartJoinItem
                val hash = uiCartJoinItem.hash
                val amount = uiCartJoinItem.amount
                cartSelectTopBodyCb.setOnClickListener {
                    Log.e(TAG, "bind: 체크박스 아이템 클릭=")
                    onClickItemClickListener?.onCheckBoxCheckedChanged(
                        adapterPosition,
                        hash,
                        uiCartJoinItem.checked
                    )
                }
                cartSelectTopBodyIbDelete.setOnClickListener {
                    onClickItemClickListener?.onClickDeleteBtn(adapterPosition, hash)
                }
                cartSelectTopBodyIbMinus.setOnClickListener {
                    if (amount > 1) {
                        onClickItemClickListener?.onClickAmountBtn(
                            adapterPosition,
                            hash,
                            amount - 1
                        )
                    }
                }
                cartSelectTopBodyIbPlus.setOnClickListener {
                    onClickItemClickListener?.onClickAmountBtn(adapterPosition, hash, amount + 1)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCartDishTopBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder: ${currentList[position]}", )
        holder.bind(currentList[position])
    }
}