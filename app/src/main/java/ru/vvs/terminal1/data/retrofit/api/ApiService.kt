package ru.vvs.terminal1.data.retrofit.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.vvs.terminal1.model.Cart
import ru.vvs.terminal1.model.ItemsOrder

interface ApiService {

    @GET("upp2018/hs/nm/carts")
    suspend fun getCarts() : Cart

    @POST("upp2018/hs/nm/order")
    suspend fun postOrder(@Query("name") name: String, @Query("number") number: String, @Body items: List<ItemsOrder>) : Response<ItemsOrder>

}