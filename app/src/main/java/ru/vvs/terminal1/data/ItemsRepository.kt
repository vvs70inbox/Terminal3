package ru.vvs.terminal1.data

import ru.vvs.terminal1.data.room.dao.DaoItemsOrder
import ru.vvs.terminal1.model.ItemsOrder

class ItemsRepository(private val itemsDao: DaoItemsOrder) {

    suspend fun getItems(orderId: Int): List<ItemsOrder> {
        return itemsDao.getItemsOrder(orderId)
    }
}