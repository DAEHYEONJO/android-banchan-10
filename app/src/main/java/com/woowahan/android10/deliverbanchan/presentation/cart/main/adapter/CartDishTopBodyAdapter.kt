package com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartDishTopBodyBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartJoinItem
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@ActivityRetainedScoped
class CartDishTopBodyAdapter @Inject constructor(
) : ListAdapter<UiCartJoinItem, CartDishTopBodyAdapter.ViewHolder>(diffUtil) {

    interface OnCartTopBodyItemClickListener {
        fun onClickDeleteBtn(position: Int, hash: String)
        fun onCheckBoxCheckedChanged(position: Int, hash: String, checked: Boolean)
        fun onClickAmountBtn(position: Int, hash: String, amount: Int)
        fun onClickAmountTv(position: Int, amount: Int)
    }

    var onCartTopBodyItemClickListener: OnCartTopBodyItemClickListener? = null

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

    inner class ViewHolder(val binding: ItemCartDishTopBodyBinding, private val coroutineScope: CoroutineScope) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiCartJoinItem: UiCartJoinItem) {
            with(binding) {
                item = uiCartJoinItem
                val hash = uiCartJoinItem.hash
                val amount = uiCartJoinItem.amount
                cartSelectTopBodyCb.setOnClickListener {
                    onCartTopBodyItemClickListener?.onCheckBoxCheckedChanged(
                        adapterPosition, hash, uiCartJoinItem.checked
                    )
                }
                cartSelectTopBodyIbDelete.setClickEventWithDuration(coroutineScope) {
                    onCartTopBodyItemClickListener?.onClickDeleteBtn(adapterPosition, hash)
                }
                cartSelectTopBodyIbMinus.setOnClickListener {
                    if (amount > 1) {
                        onCartTopBodyItemClickListener?.onClickAmountBtn(
                            adapterPosition, hash, amount - 1
                        )
                    }
                }
                cartSelectTopBodyIbPlus.setOnClickListener {
                    if (amount <= 19) onCartTopBodyItemClickListener?.onClickAmountBtn(adapterPosition, hash, amount + 1)
                }
                with(cartSelectTopBodyTvAmount){
                    setClickEventWithDuration(coroutineScope){
                        onCartTopBodyItemClickListener?.onClickAmountTv(adapterPosition, text.toString().toInt())
                    }
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCartDishTopBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.findViewTreeLifecycleOwner()!!.lifecycleScope)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder: ${currentList[position]}", )
        holder.bind(currentList[position])
    }
}