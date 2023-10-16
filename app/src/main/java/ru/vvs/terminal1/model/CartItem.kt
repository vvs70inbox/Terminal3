package ru.vvs.terminal1.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "carts_table")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val Barcode: String,
    val Character: String,
    val Description: String,
    val DiscountGroup: String,
    val GroupString: String,
    val Height: String,
    val History: String,
    val PotSize: String,
    val Price: Int,
    val Product: String,
    val Production: Int,
    val Quantity: Int,
    val Reserve: Int
): Serializable