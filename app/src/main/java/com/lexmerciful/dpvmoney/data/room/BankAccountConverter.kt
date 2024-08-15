package com.lexmerciful.dpvmoney.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.data.model.Transaction

class BankAccountConverter {
    val gson = Gson()

    @TypeConverter
    fun bankAccountToString(bankAccount: BankAccount?): String {
        return if (bankAccount != null) {
            gson.toJson(bankAccount)
        } else {
            ""
        }
    }

    @TypeConverter
    fun stringTobankAccount(bankAccountString: String?): BankAccount? {
        return if (bankAccountString != null) {
            gson.fromJson(bankAccountString, BankAccount::class.java)
        } else {
            null
        }
    }
}