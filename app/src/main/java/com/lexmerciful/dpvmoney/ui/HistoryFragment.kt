package com.lexmerciful.dpvmoney.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lexmerciful.dpvmoney.adapters.TransactionAdapter
import com.lexmerciful.dpvmoney.data.model.Transaction
import com.lexmerciful.dpvmoney.databinding.FragmentHistoryBinding
import com.lexmerciful.dpvmoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var transactionAdapter: TransactionAdapter
    private var transactionList = mutableListOf<Transaction>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTransactionRecyclerView()

        observeAllTransactions()
    }

    private fun setupTransactionRecyclerView() {
        transactionAdapter = TransactionAdapter()
        binding.transactionRecyclerView.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeAllTransactions() {
        mainViewModel.observeAllTransactions().observe(viewLifecycleOwner) { transactionResult ->
            if (transactionResult.isEmpty()) {
                binding.emptyHistoryLinearLayout.visibility = View.VISIBLE
                binding.transactionRecyclerView.visibility = View.GONE
            } else {
                binding.emptyHistoryLinearLayout.visibility = View.GONE
                binding.transactionRecyclerView.visibility = View.VISIBLE

                transactionList = transactionResult.sortedByDescending { it.transactionDate }.toMutableList()
                transactionAdapter.differ.submitList(transactionList)
            }

        }
    }

}