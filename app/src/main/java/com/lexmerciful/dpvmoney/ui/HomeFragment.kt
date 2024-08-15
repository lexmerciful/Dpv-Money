package com.lexmerciful.dpvmoney.ui

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.adapters.BankAccountAdapter
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.databinding.FragmentHomeBinding
import com.lexmerciful.dpvmoney.viewmodel.AuthViewModel
import com.lexmerciful.dpvmoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var navController: NavController
    private lateinit var bankAccountAdapter: BankAccountAdapter
    private var bankAccountList = mutableListOf<BankAccount>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWelcome()

        populateData()

        setupAccountRecyclerView()

        observeBankAccounts()

        showLogoutDialog()
    }

    private fun setupWelcome() {
        val name = authViewModel.currentUser?.displayName ?: ""
        val welcomeText = getString(R.string.welcome_back, name)

        val spannable = SpannableStringBuilder(welcomeText).apply {
            val start = welcomeText.indexOf(name)
            val end = start + name.length
            setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        binding.homeTitleTextView.text = spannable
    }

    private fun setupAccountRecyclerView() {
        bankAccountAdapter = BankAccountAdapter()
        binding.accountRecyclerView.apply {
            adapter = bankAccountAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        bankAccountAdapter.onItemClick = { bankAccount ->

            navController = findNavController()
            val bundle = bundleOf("bank_account" to Gson().toJson(bankAccount))
            navController.navigate(R.id.action_homeFragment_to_accountDetailFragment, bundle)
            }
    }

    private fun observeBankAccounts() {
        mainViewModel.observeAllBankAccounts().observe(viewLifecycleOwner) { accounts ->
            if (accounts.isNullOrEmpty()) {
                // The database is empty, insert the predefined bank accounts
                insertBankAccounts()
            } else {
                // Update the RecyclerView with the list of bank accounts
                bankAccountAdapter.differ.submitList(accounts)
            }
        }
    }

    private fun insertBankAccounts() {
        bankAccountList.forEach { bankAccount ->
            mainViewModel.insertBankAccount(bankAccount)
        }
    }

    private fun setupSignOut() {
        authViewModel.logout()
        Snackbar.make(binding.root, "Sign Out Successful!", Snackbar.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showLogoutDialog() {
        binding.signOutMenuBtn.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
            builder
                .setMessage(requireContext().getString(R.string.sign_out))
                .setMessage("Are you sure you wish to sign out?")
                .setCancelable(false)
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setPositiveButton("Logout") { _, _ ->
                    setupSignOut()
                }
                .create()
                .show()
        }
    }

    private fun populateData() {
        bankAccountList = listOf<BankAccount>(
            BankAccount(1030067819, "Tamtam Mills", 100000F, ""),
            BankAccount(1038646497, "Marve Geezerock", 20100F, ""),
            BankAccount(1141848814, "Glosh Calebs", 145184F, ""),
            BankAccount(2068744430, "Blessing Adegoke", 503000F, ""),
            BankAccount(1234567890, "Merciful Afolabi", 7861000F, ""),
            BankAccount(2003927450, "Phelps Billie", 10480F, "")
        ).toMutableList()
    }

}