package com.lexmerciful.dpvmoney.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lexmerciful.dpvmoney.data.model.BankAccount

@Dao
interface BankAccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBankAccount(bankAccount: BankAccount)

    @Update
    fun updateBankAccount(bankAccount: BankAccount)

    @Delete
    fun deleteBankAccount(bankAccount: BankAccount)

    @Query("DELETE FROM bankAccount")
    fun deleteAllBankAccounts()

    @Query("SELECT * FROM bankAccount WHERE number = :accountNumber LIMIT 1")
    fun getBankAccountByNumber(accountNumber: Int): LiveData<BankAccount>?

    @Query("SELECT * FROM `bankAccount`")
    fun getAllBankAccount(): LiveData<List<BankAccount>>
}