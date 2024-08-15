package com.lexmerciful.dpvmoney.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJsonWithType(gsonString: String): T {
    return fromJson(gsonString, object : TypeToken<T>() {}.type)
}