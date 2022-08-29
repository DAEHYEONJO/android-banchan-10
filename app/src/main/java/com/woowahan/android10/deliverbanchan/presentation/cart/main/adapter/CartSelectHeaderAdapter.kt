package com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ItemCartSelectHeaderBinding
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartHeader
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartSelectHeaderAdapter @Inject constructor() :
    ListAdapter<UiCartHeader, CartSelectHeaderAdapter.ViewHolder>(itemCallback) {

    companion object {
        val itemCallback = object : DiffUtil.ItemCallback<UiCartHeader>() {
            override fun areItemsTheSame(oldItem: UiCartHeader, newItem: UiCartHeader): Boolean {
                return oldItem.checkBoxChecked == newItem.checkBoxChecked
            }

            override fun areContentsTheSame(oldItem: UiCartHeader, newItem: UiCartHeader): Boolean {
                return oldItem == newItem
            }
        }
        const val TAG = "CartSelectHeaderAdapter"
    }

    interface OnCartSelectHeaderItemClickListener {
        fun onClickDeleteBtn()
        fun onClickSelectedToggleBtn(checkedState: Boolean)
    }

    override fun getItemId(position: Int): Long {
        return R.layout.item_cart_select_header.toLong()
    }

    var onCartSelectHeaderItemClickListener: OnCartSelectHeaderItemClickListener? = null

    inner class ViewHolder(val binding: ItemCartSelectHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiCartHeader: UiCartHeader) {
            with(binding) {

                item = uiCartHeader

                binding.cartSelectHeaderCb.setOnClickListener {
                    onCartSelectHeaderItemClickListener?.onClickSelectedToggleBtn(uiCartHeader.checkBoxChecked)
                }

                cartSelectHeaderTvSelectDelete.setOnClickListener {
                    onCartSelectHeaderItemClickListener?.onClickDeleteBtn()
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
        holder.bind(currentList[position])
    }

}