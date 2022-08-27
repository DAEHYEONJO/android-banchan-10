package com.woowahan.android10.deliverbanchan.presentation.main.exhibition.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemExhibitionBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem
import com.woowahan.android10.deliverbanchan.presentation.base.listeners.OnDishItemClickListener

class ExhibitionAdapter() :
    ListAdapter<UiExhibitionItem, ExhibitionAdapter.ExhibitionViewHolder>(diffUtil) {

    companion object {
        const val TAG = "ExhibitionAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiExhibitionItem>() {
            override fun areItemsTheSame(
                oldItem: UiExhibitionItem,
                newItem: UiExhibitionItem
            ): Boolean {
                return oldItem.categoryId == newItem.categoryId
            }

            override fun areContentsTheSame(
                oldItem: UiExhibitionItem,
                newItem: UiExhibitionItem
            ): Boolean {
                return newItem == oldItem
            }

            override fun getChangePayload(
                oldItem: UiExhibitionItem,
                newItem: UiExhibitionItem
            ): Any? {
                return if(oldItem.uiDishItems != newItem.uiDishItems) true else null
            }
        }
    }

    var dishItemClickListener: OnDishItemClickListener? = null

    inner class ExhibitionViewHolder(private val binding: ItemExhibitionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var exhibitonHorizontalAdpater: ExhibitionHorizontalAdapter

        fun bind(uiExhibitionItem: UiExhibitionItem, position: Int) {
            Log.e(TAG, "bind, position : ${position}")
            binding.uiExhibitionItem = uiExhibitionItem

            exhibitonHorizontalAdpater = ExhibitionHorizontalAdapter().apply {
                this.onDishItemClickListener = dishItemClickListener
            }

            binding.exhibitionRvHorizontal.apply {
                if (adapter == null) {
                    Log.e("ExhibitionViewHolder", "horizontal adapter == null")
                    adapter = exhibitonHorizontalAdpater
                    layoutManager = LinearLayoutManager(binding.root.context).also {
                        it.orientation = LinearLayoutManager.HORIZONTAL
                    }
                }
            }
            (binding.exhibitionRvHorizontal.adapter as ExhibitionHorizontalAdapter).submitList(
                uiExhibitionItem.uiDishItems
            )

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionViewHolder {
        Log.e(TAG, "onCreateViewHolder")
        val binding =
            ItemExhibitionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExhibitionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExhibitionViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder")
        holder.bind(currentList[position], position)
    }

    override fun onBindViewHolder(
        holder: ExhibitionViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads[0] == true) {
                holder.bind(getItem(position), position)
            }
        }
    }

}