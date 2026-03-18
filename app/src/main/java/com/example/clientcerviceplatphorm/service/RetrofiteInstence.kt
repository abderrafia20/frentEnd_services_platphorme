package com.example.clientcerviceplatphorm.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofiteInstence {

    private const val Base_Url = "https://edison-lobar-tyron.ngrok-free.dev/"
    val instance : ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Base_Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}