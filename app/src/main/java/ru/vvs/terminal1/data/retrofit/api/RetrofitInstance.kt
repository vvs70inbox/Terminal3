package ru.vvs.terminal1.data.retrofit.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

/*
    var gson = GsonBuilder()
        .setLenient()
        .create()
*/

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://91.230.197.241:81/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}