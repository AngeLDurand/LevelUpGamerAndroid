package com.example.levelupgamer.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "compras", indices = [Index("email")])
data class CompraEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fechaMillis: Long,
    val email: String?,
    val totalClp: Int
)

@Entity(
    tableName = "compra_lineas",
    indices = [Index("purchaseId"), Index("productId")]
)
data class CompraLineaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val purchaseId: Int,
    val productId: Int,
    val title: String,
    val priceClp: Int,
    val qty: Int,
    val lineTotalClp: Int
)