package ru.vvs.terminal1.data

import ru.vvs.terminal1.data.room.dao.DaoItemsOrder
import ru.vvs.terminal1.data.room.dao.DaoItemsSale
import ru.vvs.terminal1.model.ItemsOrder
import ru.vvs.terminal1.model.ItemsSale
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.model.OrderItem
import ru.vvs.terminal1.model.Sale
import ru.vvs.terminal1.model.SaleItem

class ItemsSaleRepository(private val itemsDao: DaoItemsSale) {

    suspend fun getItems(orderId: Int): List<ItemsSale> {
        return itemsDao.getItemsSale(orderId)
    }
// ищем в объединении
    suspend fun getItemByBarcode(barcode: String, orderId: Int): ItemsSale {
        return itemsDao.getItemByBarcode(barcode, orderId)
    }
// ищем в таблице
    private suspend fun getItemSaleByBarcode(barcode: String, orderId: Int): SaleItem {
        return itemsDao.getItemSaleByBarcode(barcode, orderId)
    }

    suspend fun newItem(barcode: String, orderId: Int): ItemsSale {
        val count = 0//itemsDao.getCount()+1
        val item = SaleItem(count, orderId, barcode, 1, 0, 0)

        itemsDao.InsertItem(item)

        return itemsDao.getItemByBarcode(barcode, orderId)
    }

    suspend fun updateItem(barcode: String, orderId: Int) : ItemsSale {

        var itemOrder = getItemSaleByBarcode(barcode, orderId)
        itemOrder.counts +=1
        val item = SaleItem(itemOrder.id, orderId, barcode, itemOrder.counts, itemOrder.checks, itemOrder.price)

        itemsDao.UpdateItem(item)
        val itemsOrder = itemsDao.getItemByBarcode(barcode, orderId)

        return itemsOrder
    }

    suspend fun updateItemCount(itemsOrder: ItemsOrder, orderId: Int) {

        var itemOrder = getItemSaleByBarcode(itemsOrder.Barcode, orderId)
        itemOrder.counts = itemsOrder.counts
        val item = SaleItem(itemOrder.id, orderId, itemsOrder.Barcode, itemOrder.counts, itemOrder.checks, itemOrder.price)

        itemsDao.UpdateItem(item)

    }

    suspend fun deleteItem(barcode: String, orderId: Int) {

        var itemOrder = getItemSaleByBarcode(barcode, orderId)
        itemsDao.DeleteItem(itemOrder)
    }
    suspend fun updateOrder(sale: Sale) {
        itemsDao.UpdateOrder(sale)
    }
}