package ru.vvs.terminal1.model

import android.widget.Toast
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverters
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.vvs.terminal1.MainActivity
import ru.vvs.terminal1.data.retrofit.RetrofitInstance
import ru.vvs.terminal1.data.room.BaseDao
import ru.vvs.terminal1.data.room.Converters
import ru.vvs.terminal1.mainActivity
import java.io.Serializable
import java.lang.reflect.Type
import java.time.LocalDateTime

@Entity(tableName = "products")
@TypeConverters(Converters::class)
data class Product(
    @PrimaryKey
    val barcode: String,
    val russianName: String,
    val latinName: String?,
    val specialName: String?,
    val description: String,

    val price: Int,

    val height: String,
    val container: String,
    val history: String,
    @ColumnInfo(name = "discount_group")
    val discountGroup: String,
    @ColumnInfo(name = "group_string")
    val group: String,

    val production: Int,
    val quantity: Int,
    val reserve: Int,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: LocalDateTime,
): Serializable {
    companion object {
        suspend fun getProducts(productDao: ProductDao): List<Product> {
            return productDao.getAll().ifEmpty {
                updateProducts(productDao)
            }
        }

        suspend fun updateProducts(productDao: ProductDao): List<Product> {
            return if (MainActivity.isOnline(mainActivity)) {
                val newCarts = RetrofitInstance.api.getCarts()
                productDao.insertAll(newCarts)
                productDao.getAll()
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(mainActivity, "Отсутствует интернет!", Toast.LENGTH_LONG).show()
                }
                emptyList()
            }
        }

        // Получает товар по баркоду из базы данных или отправляет запрос
        suspend fun getCartByBarcode(productDao: ProductDao, barcode: String): Product? {
            if (!barcode.startsWith("27"))
                return null;

            var product = productDao.getByBarcode(barcode)
            if (product == null && MainActivity.isOnline(mainActivity)) {
                val productList = RetrofitInstance.api.getBarcode(barcode);
                if (productList.isNotEmpty()) {
                    productDao.insert(productList.first())
                    product = productDao.getByBarcode(barcode)
                }
            }
            return product;
        }
    }
}

@Dao
interface ProductDao: BaseDao<Product> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(carts: List<Product>)

    @Query("DELETE FROM products")
    suspend fun clearTable()

    @Query("SELECT * FROM products")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM products WHERE barcode = :barcode")
    suspend fun getByBarcode(barcode: String): Product?

    @Insert
    suspend fun insertProducts(products: List<Product>)
}

class ProductDeserializer : JsonDeserializer<Product> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Product {
        TODO("Not yet implemented")
    }
}