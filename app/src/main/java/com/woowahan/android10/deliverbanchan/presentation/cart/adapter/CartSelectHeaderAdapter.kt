package com.woowahan.android10.deliverbanchan.presentation.cart.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartSelectHeaderBinding
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CartSelectHeaderAdapter @Inject constructor() :
    RecyclerView.Adapter<CartSelectHeaderAdapter.ViewHolder>() {
    
    companion object{
        const val TAG = "CartSelectHeaderAdapter"
    }

    var selectHeaderList = emptyList<Boolean>()

    lateinit var onClick: (check: Boolean) -> Unit

    inner class ViewHolder(val binding: ItemCartSelectHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(check: Boolean) {
            with(binding){
                checked = check
                cartSelectHeaderCb.setOnClickListener{
                    Log.e(TAG, "bind: ${cartSelectHeaderCb.isChecked}", )
                    onClick.invoke(cartSelectHeaderCb.isChecked)
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