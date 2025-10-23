package com.example.levelupgamer.model

import kotlinx.serialization.Serializable


@Serializable
data class CartLinePersist(
    val productId: Int,
    val qty: Int
)



