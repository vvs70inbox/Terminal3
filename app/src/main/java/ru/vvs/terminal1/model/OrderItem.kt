package ru.vvs.terminal1.model

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import ru.vvs.terminal1.data.room.BaseDao

@Entity(
    tableName = "order_items",
    foreignKeys = [ForeignKey(entity = Order::class, parentColumns = ["id"], childColumns = ["order_id"], onDelete = CASCADE)],
    indices = [Index(value = ["order_id", "barcode"], unique = true)]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "order_id")
    val orderId: Int,
    val barcode: String,
    var amount: Int,
    @Relation(
        parentColumn = "barcode",
        entityColumn = "barcode"
    )
    val product: Product,
) {
    companion object {

    }
}


@Dao
interface OrderItemDao: BaseDao<OrderItem> {
    @Transaction
    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    suspend fun getById(orderId: Int): List<OrderItem>

    @Query("UPDATE order_items SET amount = :amount WHERE order_id == :orderId AND barcode == :barcode")
    suspend fun updateOrderItemAmount(orderId: Int, barcode: String, amount: Int)

//    @Query("SELECT * FROM products p JOIN order_items oi ON (p.barcode = oi.barcode) JOIN orders o ON (oi.order_id == o.id) WHERE oi.order_id = :orderId")
//    suspend fun getProductsByOrderId(orderId: Int): List<Product>

    @Query("DELETE FROM order_items WHERE id = :orderItemId")
    suspend fun deleteOrderItemById(orderItemId: Int)

//    @Query("SELECT * FROM products WHERE barcode = :barcode")
//    suspend fun getProductByBarcode(barcode: String): Product

//    @Query("SELECT * FROM products WHERE barcode IN(:barcodes)")
//    suspend fun getProductsByBarcodes(barcodes: List<String>): List<Product>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun deleteOrderById(orderId: Int): Order

    @Transaction
    @Query("SELECT * FROM order_items WHERE order_id == :orderId AND barcode == :barcode")
    suspend fun getByBarcode(orderId: Int, barcode: String): OrderItem

    @Query("SELECT MAX(id) FROM order_items")
    suspend fun getMaxId(): Int
}