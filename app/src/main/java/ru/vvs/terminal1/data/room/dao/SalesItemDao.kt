package ru.vvs.terminal1.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import ru.vvs.terminal1.model.Sale
import ru.vvs.terminal1.model.SaleItem

@Dao
interface SalesItemDao: BaseDao<SaleItem> {

    @Query("SELECT * from sale_items_table WHERE order_id=:orderId")
    suspend fun getAllSaleItems(orderId: Int): List<Sale>

    @Query("SELECT * from sale_items_table")
    suspend fun getAllSalesItems(): List<Sale>

    @Query("SELECT MAX(id) from sale_items_table")
    suspend fun getCount(): Int

}