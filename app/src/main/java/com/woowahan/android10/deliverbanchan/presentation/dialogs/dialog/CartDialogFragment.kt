package com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartDialogBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}