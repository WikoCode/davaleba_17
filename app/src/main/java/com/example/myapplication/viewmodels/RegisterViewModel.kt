package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.RegisterRequest
import com.example.myapplication.RegisterResponse
import com.example.myapplication.api.ApiClient
import com.example.myapplication.common.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _registerResult = MutableStateFlow<Resource<RegisterResponse>?>(null)
    val registerResult: StateFlow<Resource<RegisterResponse>?> get() = _registerResult

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _registerResult.value = Resource.Loading(loading = true)
            try {
                val response =
                    ApiClient.apiServiceRegister.register(RegisterRequest(email, password))
                if (response.isSuccessful) {
                    _registerResult.value = Resource.Success(data = response.body()!!)
                } else {
                    _registerResult.value =
                        Resource.Error(errorMessage = response.errorBody()?.string() ?: "Blank")
                }
            } catch (e: Exception) {
                _registerResult.value = Resource.Error("An error occurred: ${e.message}")
            }
            _registerResult.value = Resource.Loading(loading = false)

        }
    }
}