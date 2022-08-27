package com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ItemCartDishTopBodyBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@ActivityRetainedScoped
class CartDishTopBodyAdapter @Inject constructor(
) : ListAdapter<UiCartOrderDishJoinItem, CartDishTopBodyAdapter.ViewHolder>(diffUtil) {

    interface OnCartTopBodyItemClickListener {
        fun onClickDeleteBtn(position: Int, hash: String)
        fun onCheckBoxCheckedChanged(position: Int, hash: String, checked: Boolean)
        fun onClickAmountBtn(position: Int, hash: String, amount: Int)
        fun onClickAmountTv(position: Int, amount: Int)
    }

    var onCartTopBodyItemClickListener: OnCartTopBodyItemClickListener? = null

    companion object {
        const val TAG = "CartDishTopBodyAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiCartOrderDishJoinItem>() {
            override fun areItemsTheSame(
                oldItem: UiCartOrderDishJoinItem,
                newItem: UiCartOrderDishJoinItem
            ): Boolean {
                return oldItem.checked == newItem.checked
                        && oldItem.amount == newItem.amount
                        && oldItem.totalPrice == newItem.totalPrice
            }

            override fun areContentsTheSame(
                oldItem: UiCartOrderDishJoinItem,
                newItem: UiCartOrderDishJoinItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(
        val binding: ItemCartDishTopBodyBinding,
        private val coroutineScope: CoroutineScope
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiCartOrderDishJoinItem: UiCartOrderDishJoinItem) {
            with(binding) {
                item = uiCartOrderDishJoinItem
                val hash = uiCartOrderDishJoinItem.hash
                val amount = uiCartOrderDishJoinItem.amount
                cartSelectTopBodyCb.setOnClickListener {
                    onCartTopBodyItemClickListener?.onCheckBoxCheckedChanged(
                        adapterPosition, hash, uiCartOrderDishJoinItem.checked
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
                    if (amount <= 19) {
                        onCartTopBodyItemClickListener?.onClickAmountBtn(
                            adapterPosition,
                            hash,
                            amount + 1
                        )
                    }
                }
                with(cartSelectTopBodyTvAmount) {
                    setClickEventWithDuration(coroutineScope) {
                        onCartTopBodyItemClickListener?.onClickAmountTv(
                            adapterPosition,
                            text.toString().toInt()
                        )
                    }
                }
                executePendingBindings()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return R.layout.item_cart_dish_top_body.toLong() + position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Log.e(TAG, "탑바디 onCreateViewHolder: ")
        val binding =
            ItemCartDishTopBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.findViewTreeLifecycleOwner()!!.lifecycleScope)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Log.e(TAG, "탑바디 onBindViewHolder: ")
        holder.bind(currentList[position])
    }
}