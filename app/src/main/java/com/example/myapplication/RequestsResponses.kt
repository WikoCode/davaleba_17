package com.example.myapplication


data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val token: String)

data class RegisterRequest(val email: String, val password: String)
data class RegisterResponse(val id: Int, val token: String)
