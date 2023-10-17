package ru.vvs.terminal1.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.vvs.terminal1.model.ItemsOrder
import ru.vvs.terminal1.model.OrderItem


@Dao
interface DaoItemsOrder {

        @Query("SELECT carts_table.Product, carts_table.Character, carts_table.Barcode, carts_table.Price, orders_item_table.counts FROM orders_item_table, carts_table WHERE orders_item_table.Barcode == carts_table.Barcode AND orders_item_table.order_id == :id")
        suspend fun getItemsOrder(id: Int): List<ItemsOrder>

        @Query("SELECT carts_table.Product, carts_table.Character, carts_table.Barcode, carts_table.Price, orders_item_table.counts FROM orders_item_table, carts_table WHERE orders_item_table.Barcode == carts_table.Barcode AND orders_item_table.barcode == :barcode")
        suspend fun getItemByBarcode(barcode: String): ItemsOrder

        @Insert
        suspend fun InsertItem(orderItem: OrderItem)

        @Query("SELECT COUNT(id) from orders_item_table")
        suspend fun getCount(): Int
}