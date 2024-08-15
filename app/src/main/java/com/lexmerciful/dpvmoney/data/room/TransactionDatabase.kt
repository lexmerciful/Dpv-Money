package com.lexmerciful.dpvmoney.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.data.model.Transaction

@Database(entities = [BankAccount::class, Transaction::class], version = 1)
@TypeConverters(BankAccountConverter::class, TransactionConverter::class)
abstract class TransactionDatabase : RoomDatabase() {

    abstract fun bankAccountDao(): BankAccountDao

    abstract fun transactionDao(): TransactionDao
}