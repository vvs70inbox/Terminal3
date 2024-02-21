package ru.vvs.terminal1.model

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverters
import ru.vvs.terminal1.data.room.BaseDao
import ru.vvs.terminal1.data.room.Converters
import java.io.Serializable
import java.time.LocalDateTime

@Entity(tableName = "orders")
@TypeConverters(Converters::class)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime,
    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime,
    var positions: Int,
    var products: Int,
    var amount: Int
) : Serializable {
    companion object {
        suspend fun newOrder(ordersDao: OrderDao) : Order {
            val count = ordersDao.getMaxId()+1
            val order = Order(count, "Заказ", LocalDateTime.now(), LocalDateTime.now(), 0, 0, 0)
            ordersDao.insert(order)
            return order
        }
    }
}

@Dao
interface OrderDao: BaseDao<Order> {
    @Query("SELECT * FROM orders")
    suspend fun getAllOrders(): List<Order>

    @Query("SELECT MAX(id) FROM orders")
    suspend fun getMaxId(): Int
}