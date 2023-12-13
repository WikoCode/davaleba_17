package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.LoginRequest
import com.example.myapplication.LoginResponse
import com.example.myapplication.api.ApiClient
import com.example.myapplication.common.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginResult = MutableStateFlow<Resource<LoginResponse>?>(null)
    val loginResult: StateFlow<Resource<LoginResponse>?> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Resource.Loading(loading = true)
            try {
                val response = ApiClient.apiServiceLogin.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    _loginResult.value = Resource.Success(data = response.body()!!)
                } else {
                    _loginResult.value =
                        Resource.Error(errorMessage = response.errorBody()?.string() ?: "Blank")
                }
            } catch (e: Exception) {
                _loginResult.value = Resource.Error("An error occurred: ${e.message}")
            }
            _loginResult.value = Resource.Loading(loading = false)

        }
    }
}
