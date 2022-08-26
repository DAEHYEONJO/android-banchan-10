package com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartOrderInfoBottomBodyBinding
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartBottomBody
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@ActivityRetainedScoped
class CartOrderInfoBottomBodyAdapter @Inject constructor() :
    RecyclerView.Adapter<CartOrderInfoBottomBodyAdapter.ViewHolder>() {

    companion object {
        const val TAG = "CartOrderInfoBottomBodyAdapter"
    }

    interface OnCartBottomBodyItemClickListener {
        fun onClickOrderBtn()
    }

    var onCartBottomBodyItemClickListener: OnCartBottomBodyItemClickListener? = null

    var bottomBodyList: List<UiCartBottomBody> = List(1) { UiCartBottomBody.emptyItem() }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCartOrderInfoBottomBodyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding, parent.findViewTreeLifecycleOwner()!!.lifecycleScope)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bottomBodyList[position])
    }

    override fun getItemCount(): Int = bottomBodyList.size
}