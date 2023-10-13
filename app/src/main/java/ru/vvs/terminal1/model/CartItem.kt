package ru.vvs.terminal1.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "carts_table")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val Barcode: String,
    @ColumnInfo
    val Character: String,
    @ColumnInfo
    val Description: String,
    @ColumnInfo
    val DiscountGroup: String,
    @ColumnInfo
    val GroupString: String,
    @ColumnInfo
    val Height: String,
    @ColumnInfo
    val History: String,
    @ColumnInfo
    val PotSize: String,
    @ColumnInfo
    val Price: Int,
    @ColumnInfo
    val Product: String,
    @ColumnInfo
    val Production: Int,
    @ColumnInfo
    val Quantity: Int,
    @ColumnInfo
    val Reserve: Int
): Serializable