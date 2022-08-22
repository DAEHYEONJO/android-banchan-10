package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemExhibitionHorizontalBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.common.OnDishItemClickListener
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import kotlinx.coroutines.CoroutineScope

class ExhibitionHorizontalAdapter() : ListAdapter<UiDishItem, ExhibitionHorizontalAdapter.ViewHolder>(diffUtil) {

    companion object {
        const val TAG = "MainDishGridAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiDishItem>() {
            override fun areItemsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return newItem == oldItem
            }
        }
    }

    var onDishItemClickListener: OnDishItemClickListener? = null

    inner class ViewHolder(private val binding: ItemExhibitionHorizontalBinding, private val coroutineScope: CoroutineScope) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiDishItem: UiDishItem, position: Int) {
            binding.item = uiDishItem
            binding.viewLeft.isVisible = (position == 0)
            binding.viewRight.isVisible = (position == currentList.size - 1)
            binding.maindishImbCart.setClickEventWithDuration(coroutineScope) {
                onDishItemClickListener?.onClickCartIcon(uiDishItem)
            }
            binding.root.setClickEventWithDuration(coroutineScope) {
                onDishItemClickListener?.onClickDish(uiDishItem)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemExhibitionHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding, parent.findViewTreeLifecycleOwner()!!.lifecycleScope)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}