package ru.vvs.terminal1.data

import android.icu.util.Calendar
import ru.vvs.terminal1.data.room.dao.DaoItemsOrder
import ru.vvs.terminal1.model.ItemsOrder
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.model.OrderItem

class ItemsRepository(private val itemsDao: DaoItemsOrder) {

    suspend fun getItems(orderId: Int): List<ItemsOrder> {
        return itemsDao.getItemsOrder(orderId)
    }

    suspend fun getItemByBarcode(barcode: String): ItemsOrder {
        return itemsDao.getItemByBarcode(barcode)
    }

    suspend fun newItem(barcode: String, orderId: Int) : ItemsOrder {
        val count = itemsDao.getCount()+1
        val item = OrderItem(count, orderId, barcode, 1)

        itemsDao.InsertItem(item)
        val itemOrder = itemsDao.getItemByBarcode(barcode)

        return itemOrder
    }

}