package com.woowahan.android10.deliverbanchan.presentation.cart.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartSelectHeaderBinding
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartHeader
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartSelectHeaderAdapter @Inject constructor() :
    RecyclerView.Adapter<CartSelectHeaderAdapter.ViewHolder>() {
    
    companion object{
        const val TAG = "CartSelectHeaderAdapter"
    }

    interface OnCartTopBodyItemClickListener {
        fun onClickSelectedDelete()
        fun onClickSelectedStateChange(checkedState: Boolean)
    }
    var onCartTopBodyItemClickListener: OnCartTopBodyItemClickListener? = null
    var selectHeaderList = emptyList<UiCartHeader>()

    inner class ViewHolder(val binding: ItemCartSelectHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiCartHeader: UiCartHeader) {
            with(binding){
                item = uiCartHeader
                cartSelectHeaderCb.setOnClickListener{
                    Log.e(TAG, "bind: ${cartSelectHeaderCb.isChecked}", )
                    onCartTopBodyItemClickListener?.onClickSelectedStateChange(uiCartHeader.checkBoxChecked)
                }
                cartSelectHeaderTvSelectDelete.setOnClickListener {
                    onCartTopBodyItemClickListener?.onClickSelectedDelete()
                }
                executePendingBindings()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCartSelectHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(selectHeaderList[position])
    }

    override fun getItemCount(): Int = selectHeaderList.size
}