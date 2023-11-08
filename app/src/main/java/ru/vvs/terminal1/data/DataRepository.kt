package ru.vvs.terminal1.data

import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.vvs.terminal1.MainActivity
import ru.vvs.terminal1.data.retrofit.api.RetrofitInstance
import ru.vvs.terminal1.data.room.dao.CartsDao
import ru.vvs.terminal1.mainActivity
import ru.vvs.terminal1.model.CartItem

class DataRepository(private val cartsDao: CartsDao) {

    suspend fun getCarts(newList: Boolean): List<CartItem> {
        val cachedCarts = cartsDao.getAllCarts()
        return if (cachedCarts.isNotEmpty() && !newList) {
            cachedCarts
        } else {
            if (MainActivity.isOnline(mainActivity)) {
                val newCarts = RetrofitInstance.api.getCarts()
                cartsDao.clearTable()
                cartsDao.insertAll(newCarts)
                newCarts
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(mainActivity, "Отсутствует интернет!!!", Toast.LENGTH_LONG).show()
                }
                cachedCarts
            }
        }
    }

    // Получает товар по баркоду из базы данных или отправляет запрос
    suspend fun getCartByBarcode(barcode: String): CartItem? {
        if (!barcode.startsWith("27"))
            return null;

        var cartItem = cartsDao.getCartByBarcode(barcode)
        if (cartItem == null && MainActivity.isOnline(mainActivity)) {
            val cart = RetrofitInstance.api.getBarcode(barcode);
            if (cart.isNotEmpty()) {
                cartItem = cart.first()
                cartsDao.insert(cartItem)
            }
        }
        return cartItem;
    }

    suspend fun getCartsByProduct(product: String): List<CartItem> {
        return cartsDao.getCartsByProduct(product)
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