package ru.vvs.terminal1.data.retrofit.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vvs.terminal1.model.Cart
import ru.vvs.terminal1.model.ItemsOrder
import ru.vvs.terminal1.model.Order1C

interface ApiService {

    @GET("upp2018/hs/nm/carts")
    suspend fun getCarts() : Cart

    @POST("upp2018/hs/nm/order")
    suspend fun postOrder(@Query("name") name: String, @Query("number") number: String, @Body items: List<Order1C>) : Response<ResponseBody>

}