package com.woowahan.android10.deliverbanchan.presentation.main.exhibition.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.woowahan.android10.deliverbanchan.databinding.ItemExhibitionBinding
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
        }
    }

    var dishItemClickListener: OnDishItemClickListener? = null

    inner class ExhibitionViewHolder(private val binding: ItemExhibitionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiExhibitionItem: UiExhibitionItem) {
            binding.uiExhibitionItem = uiExhibitionItem
            val exhibitonHorizontalAdpater = ExhibitionHorizontalAdapter().apply {
                this.onDishItemClickListener = dishItemClickListener
            }
            binding.exhibitionRvHorizontal.apply {
                adapter = exhibitonHorizontalAdpater
                layoutManager = LinearLayoutManager(binding.root.context).also {
                    it.orientation = LinearLayoutManager.HORIZONTAL
                }
            }
            exhibitonHorizontalAdpater.submitList(uiExhibitionItem.uiDishItems.toList())

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionViewHolder {
        val binding =
            ItemExhibitionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExhibitionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExhibitionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}