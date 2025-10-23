package com.example.levelupgamer.model

data class CartUiState(
    val items: List<CartItemUi> = emptyList(),
    val totalQty: Int = 0,
    val totalClp: Int = 0,
    val isEmpty: Boolean = true
)