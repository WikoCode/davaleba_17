package com.example.myapplication.api


import com.example.myapplication.LoginRequest
import com.example.myapplication.LoginResponse
import com.example.myapplication.RegisterRequest
import com.example.myapplication.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceLogin {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}

interface ApiServiceRegister {

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

}