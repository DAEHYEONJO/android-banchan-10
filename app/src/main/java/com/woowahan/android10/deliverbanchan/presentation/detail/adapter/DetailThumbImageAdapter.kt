package com.woowahan.android10.deliverbanchan.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ItemDetailThumbImageRvBinding

class DetailThumbImageAdapter(
    private val changeCurrentPage: (pageNum : Int) -> Unit
) :
    ListAdapter<List<String>, DetailThumbImageAdapter.DetailThumbImageHolder>(
        DetailThumbImageDiffUtil
    ) {

    var currentImagePage = 0

    inner class DetailThumbImageHolder(private val binding: ItemDetailThumbImageRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrlList: List<String>, changeCurrentPage: (pageNum: Int) -> Unit) {
            val vpAdapter = DetailThumbViewPagerAdapter()
            binding.detailVpThumb.apply {
                adapter = vpAdapter
                registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        currentImagePage = position
                        changeCurrentPage(position)
                    }
                })
            }

            TabLayoutMediator(binding.detailTl, binding.detailVpThumb) { tab, position ->
                tab.text = "  "
            }.attach()

            vpAdapter.submitList(imgUrlList.toList())

            imgUrlList.forEachIndexed { index, url ->
                val textView = LayoutInflater.from(binding.root.context)
                    .inflate(R.layout.tab_indicator, null) as TextView
                binding.detailTl.getTabAt(index)?.customView = textView
            }

            binding.detailVpThumb.setCurrentItem(currentImagePage, false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailThumbImageHolder {
        val binding =
            ItemDetailThumbImageRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DetailThumbImageHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailThumbImageHolder, position: Int) {
        holder.bind(getItem(position), changeCurrentPage)
    }

    companion object DetailThumbImageDiffUtil : DiffUtil.ItemCallback<List<String>>() {
        override fun areItemsTheSame(oldItem: List<String>, newItem: List<String>) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: List<String>, newItem: List<String>) =
            oldItem == newItem
    }
}