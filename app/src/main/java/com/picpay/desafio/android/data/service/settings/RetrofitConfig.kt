package com.picpay.desafio.android.data.service.settings

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val URL = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"

private val okHttp: OkHttpClient by lazy {
    OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }).build()
}

 val retrofit: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl(URL)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
}