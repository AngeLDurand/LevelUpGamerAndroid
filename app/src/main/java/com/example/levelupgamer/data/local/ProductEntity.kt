package com.example.levelupgamer.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "productos",
    indices = [Index("categoriaSlug")]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val precioClp: Int,
    val imagenUrl: String,
    val thumbUrl: String,
    val categoriaSlug: String
)
