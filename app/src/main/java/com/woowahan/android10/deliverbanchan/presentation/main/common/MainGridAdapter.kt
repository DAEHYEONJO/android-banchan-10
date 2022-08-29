package com.woowahan.android10.deliverbanchan.presentation.main.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemSoupBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.listeners.OnDishItemClickListener
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@FragmentScoped
class MainGridAdapter @Inject constructor() :
    ListAdapter<UiDishItem, MainGridAdapter.ViewHolder>(diffUtil) {

    var onDishItemClickListener: OnDishItemClickListener? = null

    companion object {
        const val TAG = "MainGridAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiDishItem>() {
            override fun areItemsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return newItem == oldItem
            }

            override fun getChangePayload(oldItem: UiDishItem, newItem: UiDishItem): Any? {
                return if (oldItem.isInserted != newItem.isInserted) true else null
            }
        }
    }

    inner class ViewHolder(
        private val binding: ItemSoupBinding,
        private val coroutineScope: CoroutineScope
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiDishItem: UiDishItem) {
            with(binding) {
                item = uiDishItem
                soupImbCart.setClickEventWithDuration(coroutineScope) {
                    onDishItemClickListener?.onClickCartIcon(uiDishItem)
                }
                root.setClickEventWithDuration(coroutineScope) {
                    onDishItemClickListener?.onClickDish(uiDishItem)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSoupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.findViewTreeLifecycleOwner()?.lifecycleScope!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads[0] == true) {
                currentList[position]?.let { holder.bind(it) }

            }
        }
    }
}