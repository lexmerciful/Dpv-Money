package com.lexmerciful.dpvmoney.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.lexmerciful.dpvmoney.data.repository.AuthRepository
import com.lexmerciful.dpvmoney.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginLiveData = MutableLiveData<Resource<FirebaseUser>?>()
    val loginLiveData: LiveData<Resource<FirebaseUser>?> = _loginLiveData

    private val _signupLiveData = MutableLiveData<Resource<FirebaseUser>?>()
    val signupLiveData: LiveData<Resource<FirebaseUser>?> = _signupLiveData

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser != null) {
            _loginLiveData.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun signInUser(email: String, password: String)  {
        _loginLiveData.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginLiveData.postValue(result)
        }
    }

    fun signUpUser(name: String, email: String, password: String) {
        _signupLiveData.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.signup(name, email, password)
            _signupLiveData.postValue(result)
        }
    }

    fun logout() {
        repository.logout()
        _loginLiveData.value = null
        _signupLiveData.value = null
    }
}