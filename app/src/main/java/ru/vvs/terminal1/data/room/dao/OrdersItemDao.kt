package ru.vvs.terminal1.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import ru.vvs.terminal1.model.OrderItem

@Dao
interface OrdersItemDao: BaseDao<OrderItem> {

    @Query("SELECT * from orders_item_table WHERE order_id = :orderId")
    suspend fun getAllItemsByOrderId(orderId: Int): List<OrderItem>

}