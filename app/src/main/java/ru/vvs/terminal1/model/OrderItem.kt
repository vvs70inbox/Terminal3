package ru.vvs.terminal1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders_item_table")
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val orderId: Int,
    val barcode: String,
    val count: Int
)
