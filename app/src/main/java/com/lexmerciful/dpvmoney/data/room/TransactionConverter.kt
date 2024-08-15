package com.lexmerciful.dpvmoney.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.lexmerciful.dpvmoney.data.model.Transaction

class TransactionConverter {
    val gson = Gson()

    @TypeConverter
    fun transactionToString(transaction: Transaction?): String {
        return if (transaction != null) {
            gson.toJson(transaction)
        } else {
            ""
        }
    }

    @TypeConverter
    fun stringToTransaction(transactionString: String?): Transaction? {
        return if (transactionString != null) {
            gson.fromJson(transactionString, Transaction::class.java)
        } else {
            null
        }
    }
}