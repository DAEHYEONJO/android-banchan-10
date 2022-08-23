package com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog

import android.app.Dialog
import android.content.Context
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartDialogFragment @Inject constructor(): DialogFragment() {

    interface TextClickListener {
        fun moveToCartTextClicked()
    }

    private var _binding: FragmentCartDialogBinding? = null
    private val binding: FragmentCartDialogBinding get() = checkNotNull(_binding)
    private lateinit var textClickListener: TextClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            textClickListener = context as TextClickListener
        }catch (e: ClassCastException){
            throw ClassCastException("dialog Fragment cast exception")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartDialogBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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
}