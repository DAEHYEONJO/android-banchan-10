package com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentNumberDialogBinding
import com.woowahan.android10.deliverbanchan.presentation.common.ext.onClickCallBackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class NumberDialogFragment : DialogFragment() {

    interface OnNumberDialogClickListener {
        fun onClickAmountChangeBtn(position: Int, amount: Int)
    }
    private var _binding: FragmentNumberDialogBinding? = null
    private val binding get() = requireNotNull(_binding)
    private var onNumberDialogClickListener: OnNumberDialogClickListener? = null
    private var curNumberPickerValue = 0
    private var cartPosition = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onNumberDialogClickListener = context as OnNumberDialogClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartPosition = arguments?.getInt("position", 0)!!
        curNumberPickerValue = if (savedInstanceState?.getInt("curNumberPickerValue") == null){
            arguments?.getInt("amount", 0)!!
        }else{
            savedInstanceState.getInt("curNumberPickerValue", 0)
        }
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNumberDialogBinding.inflate(layoutInflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        with(binding){
            with(numberPickerDialogNpAmount){
                minValue = 1
                maxValue = 20
                value = curNumberPickerValue
            }
            btnGoToCart.setOnClickListener {
                onNumberDialogClickListener?.onClickAmountChangeBtn(cartPosition, numberPickerDialogNpAmount.value)
                dismiss()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("curNumberPickerValue", binding.numberPickerDialogNpAmount.value)
    }

}