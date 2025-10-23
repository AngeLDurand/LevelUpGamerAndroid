package com.example.levelupgamer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("SELECT * FROM productos WHERE categoriaSlug = :slug ORDER BY id DESC")
    suspend fun getByCategory(slug: String): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ProductEntity>)

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun count(): Int

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ProductEntity?

    @Query("SELECT * FROM productos WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Int>): List<ProductEntity>

    @Query("SELECT * FROM productos ORDER BY id DESC")
    suspend fun getAllDesc(): List<ProductEntity>

}
