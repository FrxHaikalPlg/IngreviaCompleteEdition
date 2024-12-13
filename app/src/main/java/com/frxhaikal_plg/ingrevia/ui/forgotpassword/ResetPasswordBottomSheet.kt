package com.frxhaikal_plg.ingrevia.ui.forgotpassword

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.frxhaikal_plg.ingrevia.databinding.BottomSheetResetPasswordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResetPasswordBottomSheet(private val resetLink: String) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetResetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnResetPassword.setOnClickListener {
            openResetLink()
            dismiss()
        }
    }

    private fun openResetLink() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resetLink))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ResetPasswordBottomSheet"
    }
} 