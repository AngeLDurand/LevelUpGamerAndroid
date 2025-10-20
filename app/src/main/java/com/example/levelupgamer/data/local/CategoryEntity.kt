package com.example.levelupgamer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class CategoryEntity(
    @PrimaryKey val slug: String,
    val nombre: String
)
