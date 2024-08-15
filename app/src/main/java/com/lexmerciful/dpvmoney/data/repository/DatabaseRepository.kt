package com.lexmerciful.dpvmoney.data.repository

import androidx.lifecycle.LiveData
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.data.model.Transaction
import com.lexmerciful.dpvmoney.data.room.BankAccountDao
import com.lexmerciful.dpvmoney.data.room.TransactionDao
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val bankAccountDao: BankAccountDao,
    private val transactionDao: TransactionDao
) {

    fun insertbankAccount(bankAccount: BankAccount) {
        bankAccountDao.insertBankAccount(bankAccount)
    }

    fun updateBankAccount(bankAccount: BankAccount) {
        bankAccountDao.updateBankAccount(bankAccount)
    }

    fun deletebankAccount(bankAccount: BankAccount) {
        bankAccountDao.deleteBankAccount(bankAccount)
    }

    fun getBankAccountByNumber(accountNumber: Int): LiveData<BankAccount>? {
        return bankAccountDao.getBankAccountByNumber(accountNumber)
    }

    fun observeAllBankAccounts(): LiveData<List<BankAccount>> {
        return bankAccountDao.getAllBankAccount()
    }

    fun insertTransaction(transaction: Transaction) {
        transactionDao.upsertTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    fun observeAllTransactions(): LiveData<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }

}