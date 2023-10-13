package ru.vvs.terminal1.data.retrofit.api

import retrofit2.Response
import retrofit2.http.GET
import ru.vvs.terminal1.model.Cart

interface ApiService {

    @GET("upp2018/hs/nm/carts")
    suspend fun getCarts() : Cart

}