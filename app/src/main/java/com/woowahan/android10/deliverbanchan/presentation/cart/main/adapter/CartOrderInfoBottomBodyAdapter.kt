package com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ItemCartOrderInfoBottomBodyBinding
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartBottomBody
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@ActivityRetainedScoped
class CartOrderInfoBottomBodyAdapter @Inject constructor() : ListAdapter<UiCartBottomBody, CartOrderInfoBottomBodyAdapter.ViewHolder>(
    itemCallback) {

    companion object {
        val itemCallback = object : DiffUtil.ItemCallback<UiCartBottomBody>(){
            override fun areItemsTheSame(
                oldItem: UiCartBottomBody,
                newItem: UiCartBottomBody
            ): Boolean {
                return oldItem.totalPrice == newItem.totalPrice
            }

            override fun areContentsTheSame(
                oldItem: UiCartBottomBody,
                newItem: UiCartBottomBody
            ): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(
                oldItem: UiCartBottomBody,
                newItem: UiCartBottomBody
            ): Any? {
                return if (oldItem.totalPrice!=newItem.totalPrice) true else null
            }
        }
        const val TAG = "CartOrderInfoBottomBodyAdapter"
    }

    interface OnCartBottomBodyItemClickListener {
        fun onClickOrderBtn()
    }

    var onCartBottomBodyItemClickListener: OnCartBottomBodyItemClickListener? = null

    inner class ViewHolder(
        val binding: ItemCartOrderInfoBottomBodyBinding,
        private val coroutineScope: CoroutineScope
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiCartBottomBody: UiCartBottomBody) {
            with(binding) {
                item = uiCartBottomBody
                cartOrderBottomBodyBtnOrder.setClickEventWithDuration(coroutineScope) {
                    onCartBottomBodyItemClickListener?.onClickOrderBtn()
                }
                executePendingBindings()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return R.layout.item_cart_order_info_bottom_body.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Log.e(TAG, "바텀 바디 onCreateViewHolder: ", )
        val binding =
            ItemCartOrderInfoBottomBodyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding, parent.findViewTreeLifecycleOwner()!!.lifecycleScope)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Log.e(TAG, "바텀 바디 onBindViewHolder: ", )
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            if (payloads[0]==true){
                currentList[position]?.let {
                    holder.bind(it)
                }
            }
        }
    }

}