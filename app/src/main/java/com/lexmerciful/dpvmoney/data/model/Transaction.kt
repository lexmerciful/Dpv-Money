package com.lexmerciful.dpvmoney.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val sourceAccount: BankAccount,
    val destinationAccount: BankAccount,
    val amount: Float,
    val transactionDate: String
)
