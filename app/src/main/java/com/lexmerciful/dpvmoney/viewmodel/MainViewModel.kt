package com.lexmerciful.dpvmoney.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.data.model.Transaction
import com.lexmerciful.dpvmoney.data.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DatabaseRepository
) : ViewModel() {

    private val bankAccounts = repository.observeAllBankAccounts()

    private val transactions = repository.observeAllTransactions()

    fun observeAllBankAccounts(): LiveData<List<BankAccount>> {
        return bankAccounts
    }

    fun observeAllTransactions(): LiveData<List<Transaction>> {
        return transactions
    }

    fun insertBankAccount(bankAccount: BankAccount) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertbankAccount(bankAccount)
    }

    fun updateBankAccount(bankAccount: BankAccount) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateBankAccount(bankAccount)
    }

    fun getBankAccountByNumber(accountNumber: Int) = repository.getBankAccountByNumber(accountNumber)

    fun deleteBankAccount(bankAccount: BankAccount) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletebankAccount(bankAccount)
    }

    fun insertTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTransaction(transaction)
    }

}