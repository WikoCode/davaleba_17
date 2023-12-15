package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val token: String)

@Parcelize
data class RegisterRequest(val email: String, val password: String) : Parcelable

data class RegisterResponse(val id: Int, val token: String)
