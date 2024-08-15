package com.lexmerciful.dpvmoney.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.data.model.Transaction
import com.lexmerciful.dpvmoney.databinding.FragmentPaymentBinding
import com.lexmerciful.dpvmoney.utils.Utils
import com.lexmerciful.dpvmoney.utils.fromJsonWithType
import com.lexmerciful.dpvmoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMakePaymentButton()
    }

    private fun setupMakePaymentButton() {
        binding.makeTransferCardView.setOnClickListener {
            val intent = Intent(requireContext(), MakeTransferActivity::class.java)
            startActivityForResult(intent, TRANSFER_CODE)
        }
    }

    private fun updateBalances(transaction: Transaction) {
        transaction.sourceAccount.let { source ->
            transaction.destinationAccount.let { destination ->

                val updatedSourceAccount = source.copy(
                    balance = source.balance - transaction.amount,
                    lastTransactionDate = transaction.transactionDate
                )

                val updatedDestinationAccount = destination.copy(
                    balance = destination.balance + transaction.amount,
                    lastTransactionDate = transaction.transactionDate
                )

                mainViewModel.updateBankAccount(updatedSourceAccount)
                mainViewModel.updateBankAccount(updatedDestinationAccount)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TRANSFER_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val transactionString = data?.getStringExtra(Utils.TRANSACTION)
            val newTransaction = Gson().fromJsonWithType<Transaction>(transactionString!!)
            val isTransferSuccessful = data.getBooleanExtra(Utils.IS_TRANSFER_SUCCESSFUL, false)

            Log.d(Utils.TRANSACTION, "isTransferSuccessful == $isTransferSuccessful, trans == $newTransaction")
            if (isTransferSuccessful) {
                val bottomNavView = requireActivity().findViewById<BottomNavigationView>(R.id.btm_nav)
                bottomNavView.selectedItemId = R.id.historyFragment

                mainViewModel.insertTransaction(newTransaction)
                updateBalances(newTransaction)

                val successFragment = PaymentSuccessFragment(
                    "NGN",
                    newTransaction.amount
                )
                successFragment.isCancelable = false
                successFragment.show(parentFragmentManager, TAG)
            }
        }
    }


    companion object {
        const val TRANSFER_CODE = 101
        val TAG = PaymentFragment::class.simpleName
    }

}