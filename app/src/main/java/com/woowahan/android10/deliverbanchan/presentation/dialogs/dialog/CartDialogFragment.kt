package com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartDialogBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartDialogFragment : DialogFragment() {

    private var _binding: FragmentCartDialogBinding? = null
    private val binding: FragmentCartDialogBinding get() = checkNotNull(_binding)
    private val cartDialogViewModel: CartDialogFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartDialogBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        arguments?.let {
            val hash = it.getString("hash", "")
            val title = it.getString("title", "")
            Log.e("CartDialogFragment", "hash : ${hash}, title : ${title}")

            cartDialogViewModel.currentHash = hash
            cartDialogViewModel.currentTitle = title
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMoveToCartTextClickEvent()
        setDismissTextClickEvent()
    }

    private fun setMoveToCartTextClickEvent() {
        binding.dialogTvMoveToCart.setOnClickListener {
            textClickListener.moveToCartTextClicked()
            dismiss()
        }
    }

    private fun setDismissTextClickEvent() {
        binding.dialogTvDismiss.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface TextClickListener {
        fun moveToCartTextClicked()
    }

    fun setTextClickListener(textClickListener: TextClickListener) {
        this.textClickListener = textClickListener
    }

    private lateinit var textClickListener: TextClickListener
}