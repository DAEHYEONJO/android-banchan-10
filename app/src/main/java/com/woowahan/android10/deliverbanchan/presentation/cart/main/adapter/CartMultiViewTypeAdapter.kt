package com.woowahan.android10.deliverbanchan.presentation.cart.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowahan.android10.deliverbanchan.databinding.ItemCartDishTopBodyBinding
import com.woowahan.android10.deliverbanchan.databinding.ItemCartOrderInfoBottomBodyBinding
import com.woowahan.android10.deliverbanchan.databinding.ItemCartSelectHeaderBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiCartMultiViewType
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartBottomBody
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartHeader
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@ActivityRetainedScoped
class CartMultiViewTypeAdapter @Inject constructor() :
    ListAdapter<UiCartMultiViewType, RecyclerView.ViewHolder>(diffUtil) {

    interface OnCartMultiViewTypeClickInterface {
        fun onClickOrderBtn()
        fun onClickHeaderDeleteBtn()
        fun onClickHeaderSelectedToggleBtn(checkState: Boolean)
        fun onClickBodyDeleteBtn(position: Int, hash: String)
        fun onClickBodyCheckBox(
            position: Int,
            hash: String,
            checked: Boolean
        )
        fun onClickBodyAmountBtn(position: Int, hash: String, amount: Int)
        fun onClickBodyAmountTv(position: Int, amount: Int)
    }

    var onCartMultiViewTypeClickInterface: OnCartMultiViewTypeClickInterface? = null

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UiCartMultiViewType>() {
            override fun areItemsTheSame(
                oldItem: UiCartMultiViewType,
                newItem: UiCartMultiViewType
            ): Boolean {
                return oldItem.uiCartHeader?.checkBoxChecked == newItem.uiCartHeader?.checkBoxChecked
                        && oldItem.uiCartOrderDishJoinItem?.checked == newItem.uiCartOrderDishJoinItem?.checked
                        && oldItem.uiCartOrderDishJoinItem?.amount == newItem.uiCartOrderDishJoinItem?.amount
                        && oldItem.uiCartOrderDishJoinItem?.totalPrice == newItem.uiCartOrderDishJoinItem?.totalPrice
                        && oldItem.uiCartBottomBody?.totalPrice == newItem.uiCartBottomBody?.totalPrice
            }

            override fun areContentsTheSame(
                oldItem: UiCartMultiViewType,
                newItem: UiCartMultiViewType
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class HeaderViewHolder(val binding: ItemCartSelectHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiCartHeader: UiCartHeader) {
            Log.e("멀티퓨타입 어탭터", "헤더 $uiCartHeader", )
            with(binding) {
                item = uiCartHeader
                binding.cartSelectHeaderCb.setOnClickListener {
                    onCartMultiViewTypeClickInterface?.onClickHeaderSelectedToggleBtn(uiCartHeader.checkBoxChecked)
                }
                cartSelectHeaderTvSelectDelete.setOnClickListener {
                    onCartMultiViewTypeClickInterface?.onClickHeaderDeleteBtn()
                }
                executePendingBindings()
            }
        }
    }

    inner class BodyViewHolder(
        val binding: ItemCartDishTopBodyBinding,
        private val coroutineScope: CoroutineScope
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiCartOrderDishJoinItem: UiCartOrderDishJoinItem) {
            Log.e("멀티퓨타입 어탭터", "바디 $uiCartOrderDishJoinItem", )
            with(binding) {
                item = uiCartOrderDishJoinItem
                val hash = uiCartOrderDishJoinItem.hash
                val amount = uiCartOrderDishJoinItem.amount
                cartSelectTopBodyCb.setOnClickListener {
                    onCartMultiViewTypeClickInterface?.onClickBodyCheckBox(
                        adapterPosition, hash, uiCartOrderDishJoinItem.checked
                    )
                }
                cartSelectTopBodyIbDelete.setClickEventWithDuration(coroutineScope) {
                    onCartMultiViewTypeClickInterface?.onClickBodyDeleteBtn(adapterPosition, hash)
                }
                cartSelectTopBodyIbMinus.setOnClickListener {
                    if (amount > 1) {
                        onCartMultiViewTypeClickInterface?.onClickBodyAmountBtn(
                            adapterPosition, hash, amount - 1
                        )
                    }
                }
                cartSelectTopBodyIbPlus.setOnClickListener {
                    if (amount <= 19) {
                        onCartMultiViewTypeClickInterface?.onClickBodyAmountBtn(
                            adapterPosition,
                            hash,
                            amount + 1
                        )
                    }
                }
                with(cartSelectTopBodyTvAmount) {
                    setClickEventWithDuration(coroutineScope) {
                        onCartMultiViewTypeClickInterface?.onClickBodyAmountTv(
                            adapterPosition,
                            text.toString().toInt()
                        )
                    }
                }
                executePendingBindings()
            }
        }
    }

    inner class FooterViewHolder(
        val binding: ItemCartOrderInfoBottomBodyBinding,
        private val coroutineScope: CoroutineScope
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiCartBottomBody: UiCartBottomBody) {
            Log.e("멀티퓨타입 어탭터", "푸터 $uiCartBottomBody", )
            with(binding) {
                item = uiCartBottomBody
                cartOrderBottomBodyBtnOrder.setClickEventWithDuration(coroutineScope) {
                    onCartMultiViewTypeClickInterface?.onClickOrderBtn()
                }
                executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            UiCartMultiViewType.HEADER -> {
                HeaderViewHolder(
                    ItemCartSelectHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            UiCartMultiViewType.BODY -> {
                BodyViewHolder(
                    ItemCartDishTopBodyBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    parent.findViewTreeLifecycleOwner()!!.lifecycleScope
                )
            }
            else -> {
                FooterViewHolder(
                    ItemCartOrderInfoBottomBodyBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    parent.findViewTreeLifecycleOwner()!!.lifecycleScope
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            UiCartMultiViewType.HEADER -> {
                Log.e("멀티뷰타입 바인드 뷰 홀더", "onBindViewHolder: 헤더", )
                currentList[position].uiCartHeader?.let {
                    (holder as HeaderViewHolder).bind(it)
                }
            }
            UiCartMultiViewType.BODY -> {
                Log.e("멀티뷰타입 바인드 뷰 홀더", "onBindViewHolder: 바디", )
                currentList[position].uiCartOrderDishJoinItem?.let {
                    (holder as BodyViewHolder).bind(it)
                }
            }
            else -> {
                Log.e("멀티뷰타입 바인드 뷰 홀더", "onBindViewHolder: 푸터", )
                currentList[position].uiCartBottomBody?.let {
                    (holder as FooterViewHolder).bind(it)
                }
            }
        }
    }

}
