package com.example.levelupgamer.model

data class CartItemUi(
    val id: Int,
    val title: String,
    val priceClp: Int,
    val image: String,
    val qty: Int,
    val lineTotalClp: Int
)