package com.lexmerciful.dpvmoney.data.repository

import com.google.firebase.auth.FirebaseUser
import com.lexmerciful.dpvmoney.data.Resource

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    fun logout()
}