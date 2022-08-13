package com.woowahan.android10.deliverbanchan.presentation.main.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemSoupBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class MainGridAdapter @Inject constructor() :
    ListAdapter<UiDishItem, MainGridAdapter.ViewHolder>(diffUtil) {

    lateinit var cartIconClick: (uiDishItem: UiDishItem) -> Unit

    companion object {
        const val TAG = "MainGridAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<UiDishItem>() {
            override fun areItemsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return oldItem.hash == newItem.hash
            }

            override fun areContentsTheSame(oldItem: UiDishItem, newItem: UiDishItem): Boolean {
                return newItem == oldItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemSoupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiDishItem: UiDishItem) {
            binding.item = uiDishItem
            binding.soupImbCart.setOnClickListener {
                cartIconClick.invoke(uiDishItem)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSoupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}