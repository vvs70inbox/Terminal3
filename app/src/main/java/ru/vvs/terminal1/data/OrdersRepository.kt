package ru.vvs.terminal1.data

import android.icu.util.Calendar
import ru.vvs.terminal1.data.room.dao.OrdersDao
import ru.vvs.terminal1.model.Order

class OrdersRepository(private val ordersDao: OrdersDao) {

    suspend fun getOrders(newList: Boolean): List<Order> {
        return ordersDao.getAllOrders()
    }
    suspend fun newOrder() : Order {
        val count = ordersDao.getCount()+1
        val order = Order(count, "Заказ", count.toString(), Calendar.getInstance().time.toString(), 0, 0, 0)

        ordersDao.insert(order)
        return order
    }
}