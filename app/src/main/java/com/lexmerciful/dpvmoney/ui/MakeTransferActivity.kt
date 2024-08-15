package com.lexmerciful.dpvmoney.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.afollestad.vvalidator.form
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.data.model.Transaction
import com.lexmerciful.dpvmoney.databinding.ActivityMakeTransferBinding
import com.lexmerciful.dpvmoney.utils.Utils
import com.lexmerciful.dpvmoney.utils.Utils.IS_TRANSFER_SUCCESSFUL
import com.lexmerciful.dpvmoney.utils.Utils.TRANSACTION
import com.lexmerciful.dpvmoney.utils.fromJsonWithType
import com.lexmerciful.dpvmoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MakeTransferActivity : BaseActivity() {

    private lateinit var binding: ActivityMakeTransferBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private var bankAccountList = mutableListOf<BankAccount>()
    private lateinit var transaction: Transaction
    private lateinit var sourceAccount: BankAccount
    private lateinit var destinationAccount: BankAccount
    private var sourceAccountName = ""
    private var destinatioAccountName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        observeAllBankAccounts()

        //setupSelectSouceAccountSpinner()

        //setupSelectDestinationAccountSpinner()

        setupForm()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.makeTransferToolbar)
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun observeAllBankAccounts() {
        mainViewModel.observeAllBankAccounts().observe(this) { bankAccounts ->
            if (bankAccounts.isNotEmpty()) {
                bankAccountList = bankAccounts.toMutableList()
            }
            setupSelectSouceAccountSpinner()
            setupSelectDestinationAccountSpinner()
        }
    }

    private fun setupSelectSouceAccountSpinner() {
        val bankAccountNames: MutableList<String> = bankAccountList.map { it.name }.toMutableList()
        Log.d("TAG","account list2 == $bankAccountNames")
        bankAccountNames.add(0, "-- Select Source Account --")
        bankAccountNames.remove(destinatioAccountName)
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, bankAccountNames)
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.sourceAccountSpinner.adapter = adapter

        binding.sourceAccountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    sourceAccountName = parent?.getItemAtPosition(position) as String
                    val sourceBankAccount = bankAccountList.find { it.name == sourceAccountName }
                    if (sourceBankAccount != null) {
                        binding.availableBalanceTextView.visibility = View.VISIBLE
                        binding.availableBalanceTextView.text = getString(R.string._available_balance, Utils.formatAsCurrency(
                            sourceBankAccount.balance
                        ))
                        sourceAccount = sourceBankAccount
                    }
                } else {
                    binding.availableBalanceTextView.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }


    private fun setupSelectDestinationAccountSpinner() {
        val bankAccountNames: MutableList<String> = bankAccountList.map { it.name }.toMutableList()
        bankAccountNames.add(0, "-- Select Destination Account --")
        bankAccountNames.remove(sourceAccountName)
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, bankAccountNames)
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.destinationAccountSpinner.adapter = adapter

        binding.destinationAccountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0){
                    destinatioAccountName = parent?.getItemAtPosition(position) as String
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.ofHours(1))
        return formatter.format(Instant.now())
    }

    private fun setupForm() {
        form {
            useRealTimeValidation(disableSubmit = true)
            input(binding.amountEditText) {
                isNotEmpty()
                isDecimal().atLeast(100.0).description("Minimum transfer amount is N100")
                isDecimal().lessThan(50000.0).description("Maximum transfer amount is N50,000")
            }
            spinner(binding.sourceAccountSpinner) {
                selection().greaterThan(0).description("Select Source Account!")
            }
            spinner(binding.destinationAccountSpinner) {
                selection().greaterThan(0).description("Select Destination Account!")
            }

            submitWith(binding.nextBtn) {
                setupNextButton()
            }
        }
    }

    private fun setupNextButton() {
        val sourceBankAccount = bankAccountList.find { it.name == sourceAccountName }
        val destinationBankAccount = bankAccountList.find { it.name == destinatioAccountName }

        if (binding.amountEditText.text.toString().toFloat() > sourceBankAccount?.balance!!) {
            Snackbar.make(binding.root, "Amount exceeds available balance!!!", Snackbar.LENGTH_SHORT)
                .setTextColor(ContextCompat.getColor(this, R.color.red))
                .show()
            return
        }

        if (sourceBankAccount != destinationBankAccount) {
            transaction = Transaction(0, sourceBankAccount!!, destinationBankAccount!!, binding.amountEditText.text.toString().toFloat(), getCurrentTime())
            val intent = Intent(this, ConfirmTransferActivity::class.java)
                .putExtra(TRANSACTION, Gson().toJson(transaction))
            startActivityForResult(intent, TRANSFER_CODE)
        } else {
            Snackbar.make(binding.root, "Transfer not possible between same account!", Snackbar.LENGTH_SHORT)
                .setTextColor(ContextCompat.getColor(this, R.color.red))
                .show()
        }

    }

    private fun dismissActivityWithResult(transaction: Transaction) {
        val result = Intent()
            .putExtra(IS_TRANSFER_SUCCESSFUL, true)
            .putExtra(TRANSACTION, Gson().toJson(transaction))
        setResult(RESULT_OK, result)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TRANSFER_CODE && resultCode == RESULT_OK) {
            val transactionString = data?.getStringExtra(TRANSACTION)
            val newTransaction = Gson().fromJsonWithType<Transaction>(transactionString!!)
            val isTransferSuccessful = data.getBooleanExtra(IS_TRANSFER_SUCCESSFUL, false)

            Log.d(TRANSACTION, "isTransferSuccessful == $isTransferSuccessful, trans == $newTransaction")
            if (isTransferSuccessful) {
                dismissActivityWithResult(newTransaction)
            }
        }
    }

    companion object {
        const val TRANSFER_CODE = 101
    }
}