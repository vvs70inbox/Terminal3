package ru.vvs.terminal1.data.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vvs.terminal1.model.Product
import ru.vvs.terminal1.model.ProductDeserializer

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://91.230.197.241:81/")
            .addConverterFactory(buildGsonConverterFactory())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

private fun buildGsonConverterFactory(): GsonConverterFactory {
    val gsonBuilder = GsonBuilder()
    // Custom Product converter for Retrofit
    gsonBuilder.registerTypeAdapter(Product::class.java, ProductDeserializer())
    return GsonConverterFactory.create(gsonBuilder.create())
}