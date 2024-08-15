package com.lexmerciful.dpvmoney.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {

    fun getReformattedDate(date: String): String {

        // Log.d(TAG, "Date String: $contributionDate")

        var time = ""
        if (date.contains("T")) {
            // 2022-01-16T22:42:52.000Z
            date.replaceAfterLast(".", "")
            time = reformatDate("yyyy-MM-dd'T'HH:mm:ss", date,"hh:mm aa")
        }
        return reformatDate("yyyy-MM-dd", date,"d MMM yyyy") + "\n"+ time
    }

    private fun reformatDate(serverDateFormat: String, dateToFormat: String, newDateFormat: String): String {
        val sdf = SimpleDateFormat(serverDateFormat, Locale.getDefault())
        val date =  sdf.parse(dateToFormat)!!
        val mDateFormat = SimpleDateFormat(newDateFormat, Locale.getDefault())
        return mDateFormat.format(date)
    }

    fun formatAsCurrency(amount: Float): String {
        val formatter = DecimalFormat("#,###,##0.00")
//        if (amount.toString().toFloat() < 1) {
//            formatter = DecimalFormat("0.00")
//        }
        val currentValue = formatter.format(amount)
        return "NGN $currentValue"
    }

    const val TRANSACTION = "transaction"
    const val IS_TRANSFER_SUCCESSFUL = "is_transfer_successful"
}