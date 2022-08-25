package com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartDialogBinding
import com.woowahan.android10.deliverbanchan.presentation.base.listeners.OnCartDialogClickListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartDialogFragment @Inject constructor(): DialogFragment() {

    private var _binding: FragmentCartDialogBinding? = null
    private val binding: FragmentCartDialogBinding get() = checkNotNull(_binding)
    private lateinit var onCartDialogClickListener: OnCartDialogClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onCartDialogClickListener = context as OnCartDialogClickListener
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
            onCartDialogClickListener.moveToCartTextClicked()
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