package com.woowahan.android10.deliverbanchan.presentation.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ItemSortSpinnerDropDownBinding
import com.woowahan.android10.deliverbanchan.databinding.ItemSortSpinnerSelectedBinding
import com.woowahan.android10.deliverbanchan.presentation.common.dpToPx
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class SortSpinnerAdapter @Inject constructor(
    @ApplicationContext val context: Context
): BaseAdapter() {

    var sortSpinnerList = context.resources.getStringArray(R.array.sort_spinner_item).toList().map { str ->
        SortSpinnerItem(
            name = str,
            selected = false
        )
    }

    private val dp16ToPx = dpToPx(context, 16).toInt()
    private val dp8ToPx = dpToPx(context, 8).toInt()

    override fun getCount(): Int = sortSpinnerList.size

    override fun getItem(position: Int): Any = sortSpinnerList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemSortSpinnerSelectedBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.sortSpinnerSelectedTv.text = sortSpinnerList[position].name
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemSortSpinnerDropDownBinding.inflate(LayoutInflater.from(context), parent, false)
        with(binding){
            sortSpinnerDropDownTv.text = sortSpinnerList[position].name
            sortSpinnerDropDownIv.visibility = sortSpinnerList[position].selected.let{ selected ->
                if (selected) {
                    sortSpinnerDropDownTv.setTextAppearance(R.style.Widget_TextView_NotoSansKR_GreyScaleBlack14_Medium_TextAppearance)
                    View.VISIBLE
                } else {
                    sortSpinnerDropDownTv.setTextAppearance(R.style.Widget_TextView_NotoSansKR_GreyScaleBlack14_Normal_TextAppearance)
                    View.INVISIBLE
                }
            }
            val layoutParams = (sortSpinnerDropDownTv.layoutParams as ConstraintLayout.LayoutParams)
            when (position) {
                0 -> {
                    layoutParams.setMargins(0, dp16ToPx,0, 0)
                }
                sortSpinnerList.size - 1 -> {
                    layoutParams.setMargins(0, dp8ToPx,0, dp16ToPx)
                }
                else -> {
                    layoutParams.setMargins(0, dp8ToPx,0, 0)
                }
            }
            sortSpinnerDropDownTv.layoutParams = layoutParams
        }
        return binding.root
    }


}