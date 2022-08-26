package com.woowahan.android10.deliverbanchan.presentation.dialogs.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartBottomSheetBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.CartDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CartBottomSheetFragment : BottomSheetDialogFragment() {
    
    companion object{
        const val TAG = "CartBottomSheetFragment"
    }

    private var _binding: FragmentCartBottomSheetBinding? = null
    private val binding: FragmentCartBottomSheetBinding get() = checkNotNull(_binding)
    private val cartBottomSheetViewModel: CartBottomSheetViewModel by viewModels()
    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private val cartDialogFragment: CartDialogFragment by lazy { CartDialogFragment() }

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
        binding.lifecycleOwner = this.viewLifecycleOwner

        arguments?.let {
            val uiDishItem = it.getParcelable<UiDishItem>("UiDishItem")
            uiDishItem?.let {
                cartBottomSheetViewModel.currentUiDishItem.value = it
                binding.viewModel = cartBottomSheetViewModel
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeInsertResult()
    }

    private fun initView() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun observeInsertResult() {
        cartBottomSheetViewModel.insertSuccessEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it) {
                    cartDialogFragment.show(parentFragmentManager, "CartDialog")
                    dismiss()
                } else {
                    requireContext().showToast("장바구니 담기에 실패했습니다")
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}