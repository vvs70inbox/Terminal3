package ru.vvs.terminal1.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.vvs.terminal1.model.ItemsOrder
import ru.vvs.terminal1.model.ItemsSale
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.model.OrderItem
import ru.vvs.terminal1.model.Sale
import ru.vvs.terminal1.model.SaleItem


@Dao
interface DaoItemsSale {

        @Query("SELECT carts_table.Product, carts_table.Character, carts_table.Barcode, carts_table.Price, sale_items_table.counts, sale_items_table.checks FROM sale_items_table, carts_table WHERE (sale_items_table.Barcode == carts_table.Barcode) AND  sale_items_table.order_id == :id")
        suspend fun getItemsSale(id: Int): List<ItemsSale>

        @Query("SELECT carts_table.Product, carts_table.Character, carts_table.Barcode, carts_table.Price, sale_items_table.counts, sale_items_table.checks FROM sale_items_table, carts_table WHERE (sale_items_table.Barcode == carts_table.Barcode) AND sale_items_table.barcode == :barcode AND sale_items_table.order_id == :orderId")
        suspend fun getItemByBarcode(barcode: String, orderId: Int): ItemsSale

        @Query("SELECT * FROM sale_items_table WHERE barcode == :barcode AND order_id == :orderId")
        suspend fun getItemSaleByBarcode(barcode: String, orderId: Int): SaleItem

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun InsertItem(saleItem: SaleItem)

        @Update
        suspend fun UpdateItem(saleItem: SaleItem)

        @Delete
        suspend fun DeleteItem(saleItem: SaleItem)

        @Query("SELECT MAX(id) from sale_items_table")
        suspend fun getCount(): Int

        @Update
        suspend fun UpdateOrder(sale: Sale)
}