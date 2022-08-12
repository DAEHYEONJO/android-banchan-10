package com.woowahan.android10.deliverbanchan.presentation.bottomsheet

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartBottomSheetBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCartBottomSheetBinding? = null
    private val binding: FragmentCartBottomSheetBinding get() = checkNotNull(_binding)
    private val cartBottomSheetViewModel: CartBottomSheetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBottomSheetBinding.inflate(layoutInflater, container, false)

        arguments?.let {
            val uiDishItem = it.getParcelable<UiDishItem>("UiDishItem")
            Log.e("AppTest", "$uiDishItem")

            uiDishItem?.let {
                binding.itemTitle = it.title
                binding.nPrice = it.nPrice
                binding.sPrice = it.sPrice
                binding.imageUrl = it.image
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}