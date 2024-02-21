package ru.vvs.terminal1.data.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.vvs.terminal1.model.Order1C
import ru.vvs.terminal1.model.Product

interface ApiService {

    @GET("upp2018/hs/nm/carts")
    suspend fun getCarts() : List<Product>

    @GET("upp2018/hs/nm/barcode")
    suspend fun getBarcode(@Query("barcode") barcode: String) : List<Product>

    @POST("upp2018/hs/nm/order")
    suspend fun postOrder(@Query("name") name: String, @Query("number") number: String, @Body items: List<Order1C>) : Response<ResponseBody>

//    @GET("upp2018/hs/nm/sale")
//    suspend fun getSales(@Query("date") dateStr: String) : SaleImport

//    @GET("upp2018/hs/nm/sale_items")
//    suspend fun getSaleItems(@Query("number") numberStr: String, @Query("date") dateStr: String) : List<SaleItem>
}