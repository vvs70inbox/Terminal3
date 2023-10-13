package ru.vvs.terminal1.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import ru.vvs.terminal1.model.CartItem
import ru.vvs.terminal1.model.Order

@Dao
interface OrdersDao: BaseDao<Order> {

    @Query("SELECT * from orders_table")
    suspend fun getAllOrders(): List<Order>

}