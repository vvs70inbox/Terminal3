package ru.vvs.terminal1.model

data class ItemsOrder(
    val Product: String,
    val Character: String,
    val Barcode: String,
    var counts: Int,
    val Price: Int
)
