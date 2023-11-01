package ru.vvs.terminal1.model

data class ItemsSale(
    val Product: String,
    val Character: String,
    val Barcode: String,
    var counts: Int,
    var checks: Int,
    val Price: Int
)
