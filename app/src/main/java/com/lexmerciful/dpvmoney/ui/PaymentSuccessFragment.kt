package com.lexmerciful.dpvmoney.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.databinding.FragmentPaymentSuccessBinding
import com.lexmerciful.dpvmoney.utils.Utils

class PaymentSuccessFragment(private val currency: String, private val amount: Float) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPaymentSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    private fun setup() {
        val amountText = Utils.formatAsCurrency(amount)
        binding.amountTextView.text = amountText
        binding.successButton.setOnClickListener {
            dismiss()
        }
    }
}