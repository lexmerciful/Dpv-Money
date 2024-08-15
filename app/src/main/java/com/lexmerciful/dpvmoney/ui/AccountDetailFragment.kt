package com.lexmerciful.dpvmoney.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.adapters.TransactionAdapter
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.data.model.Transaction
import com.lexmerciful.dpvmoney.databinding.FragmentAccountDetailBinding
import com.lexmerciful.dpvmoney.utils.Utils
import com.lexmerciful.dpvmoney.utils.fromJsonWithType
import com.lexmerciful.dpvmoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountDetailFragment : Fragment() {

    private lateinit var binding: FragmentAccountDetailBinding
    private lateinit var bankAccount: BankAccount
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var transactionAdapter: TransactionAdapter
    private var transactionList = mutableListOf<Transaction>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFragmentArguments()

        populateData()

        setupTransactionRecyclerView()

        observeAllTransactions()
    }

    private fun populateData() {
        binding.accountNameTextView.text = bankAccount.name
        binding.accountNumberTextView.text = bankAccount.number.toString()
        binding.availableBalanceTextView.text = Utils.formatAsCurrency(bankAccount.balance)
    }

    private fun getFragmentArguments() {
        arguments?.getString(BANKACCOUNT)?.let { bankAccountJson ->
            bankAccount = Gson().fromJsonWithType<BankAccount>(bankAccountJson)
        }
    }

    private fun observeBankAccount() {
        mainViewModel.getBankAccountByNumber(bankAccount.number)?.observe(viewLifecycleOwner) {
            bankAccount = it
            populateData()
        }
    }

    private fun setupTransactionRecyclerView() {
        transactionAdapter = TransactionAdapter()
        transactionAdapter.showTransactionDetails = true
        transactionAdapter.userBankAccount = bankAccount
        binding.accountTransactionsRecyclerView.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeAllTransactions() {
        mainViewModel.observeAllTransactions().observe(viewLifecycleOwner) { transactionResult ->
            transactionList = transactionResult.filter { it.sourceAccount.number == bankAccount.number || it.destinationAccount.number == bankAccount.number }.toMutableList()

            if (transactionList.isEmpty()) {
                binding.emptyHistoryLinearLayout.visibility = View.VISIBLE
                binding.accountTransactionsRecyclerView.visibility = View.GONE
            } else {
                binding.emptyHistoryLinearLayout.visibility = View.GONE
                binding.accountTransactionsRecyclerView.visibility = View.VISIBLE
                transactionAdapter.differ.submitList(transactionList.sortedByDescending { it.transactionDate })
            }

        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNavView = requireActivity().findViewById<BottomNavigationView>(R.id.btm_nav)
        bottomNavView.menu.findItem(R.id.homeFragment).isChecked = true
        observeBankAccount()
    }


    companion object {
        val TAG: String = AccountDetailFragment::class.java.simpleName

        private const val BANKACCOUNT = "bank_account"

        fun newInstance(bankAccount: String) = AccountDetailFragment().apply {
            arguments = bundleOf(
                BANKACCOUNT to bankAccount
            )
        }
    }
}