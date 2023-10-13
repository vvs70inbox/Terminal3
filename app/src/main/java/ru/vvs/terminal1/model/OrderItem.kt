package ru.vvs.terminal1.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders_item_table")
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "order_id")
    val orderId: Int,
    val barcode: String,
    val count: Int
)
