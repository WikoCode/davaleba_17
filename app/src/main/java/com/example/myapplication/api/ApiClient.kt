package com.example.myapplication.api


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

    private const val BASE_URL = "https://reqres.in/api/"


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(
                        KotlinJsonAdapterFactory()
                    ).build()
                )
            )
            .build()
    }

    val apiServiceLogin: ApiServiceLogin by lazy {
        retrofit.create(ApiServiceLogin::class.java)
    }

    val apiServiceRegister: ApiServiceRegister by lazy {
        retrofit.create(ApiServiceRegister::class.java)
    }
}
