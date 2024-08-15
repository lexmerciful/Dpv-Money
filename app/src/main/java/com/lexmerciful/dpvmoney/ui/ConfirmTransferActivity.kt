package com.lexmerciful.dpvmoney.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.data.model.Transaction
import com.lexmerciful.dpvmoney.databinding.ActivityConfirmTransferBinding
import com.lexmerciful.dpvmoney.utils.Utils
import com.lexmerciful.dpvmoney.utils.fromJsonWithType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmTransferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmTransferBinding
    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        getDataFromIntent()

        populateData()

        setupConfirmButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.confirmTransferToolbar)
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


    private fun getDataFromIntent() {
        transaction = Gson().fromJsonWithType(intent.getStringExtra(Utils.TRANSACTION)!!)
    }

    private fun populateData() {
        val amount = Utils.formatAsCurrency(transaction.amount)
        binding.amountTextView.text = amount

        binding.sourceAccountTextView.text = transaction.sourceAccount.name
        binding.sourceAccountNoTextView.text = "${transaction.sourceAccount.number}"
        binding.destinationAccountTextView.text = transaction.destinationAccount.name
        binding.destinationAccountNoTextView.text = "${transaction.destinationAccount.number}"
    }

    private fun setupConfirmButton() {
        binding.nextBtn.setOnClickListener {
            dismissActivityWithResult()
        }
    }

    private fun dismissActivityWithResult() {
        val result = Intent()
            .putExtra(Utils.IS_TRANSFER_SUCCESSFUL, true)
            .putExtra(Utils.TRANSACTION, Gson().toJson(transaction))
        setResult(RESULT_OK, result)
        finish()
    }
}