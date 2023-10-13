package ru.vvs.terminal1.data

import ru.vvs.terminal1.data.retrofit.api.RetrofitInstance
import ru.vvs.terminal1.data.room.dao.CartsDao
import ru.vvs.terminal1.model.CartItem

class DataRepository(private val cartsDao: CartsDao) {

    suspend fun getCarts(newList: Boolean): List<CartItem> {
        val cachedCarts = cartsDao.getAllCarts()
        return if (cachedCarts.isNotEmpty() && !newList) {
            cachedCarts
        } else {
            val newCarts = RetrofitInstance.api.getCarts()
            cartsDao.clearTable()
            cartsDao.insertAll(newCarts)
            newCarts
        }
    }

    suspend fun getCartByBarcode(barcode: String): CartItem {
        return cartsDao.getCartByBarcode(barcode)
    }

/*   Так рекомендуют
    suspend fun getPosts(): List<Post> {
        if (postDao.getPosts().isEmpty()) {
            val posts = ApiClient.apiService.getPosts()
            postDao.insertAll(posts)
        }

        return postDao.getPosts()
    }*/

}