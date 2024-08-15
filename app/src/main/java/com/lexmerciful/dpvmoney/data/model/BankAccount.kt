package com.lexmerciful.dpvmoney.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("bankAccount")
data class BankAccount(
    @PrimaryKey(autoGenerate = true)
    val number: Int,
    val name: String,
    val balance: Float,
    val lastTransactionDate: String
)
