package com.woowahan.android10.deliverbanchan.presentation.cart.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartOrderInfoBottomBodyBinding
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartBottomBody
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartOrderInfoBottomBodyAdapter @Inject constructor() :
    RecyclerView.Adapter<CartOrderInfoBottomBodyAdapter.ViewHolder>() {

    companion object{
        const val TAG = "CartOrderInfoBottomBodyAdapter"
    }

    var bottomBodyList: List<UiCartBottomBody> = List(1){UiCartBottomBody.emptyItem ()}

    class ViewHolder(val binding: ItemCartOrderInfoBottomBodyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiCartBottomBody: UiCartBottomBody) {
            with(binding){
                Log.e(TAG, "bind: ${uiCartBottomBody.isAvailableDelivery} ${uiCartBottomBody.isAvailableFreeDelivery}")
                item = uiCartBottomBody
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCartOrderInfoBottomBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bottomBodyList[position])
    }

    override fun getItemCount(): Int = bottomBodyList.size
}