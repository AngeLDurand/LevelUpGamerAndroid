package com.example.levelupgamer.repository

import com.example.levelupgamer.data.local.CategoryDao
import com.example.levelupgamer.data.local.ProductDao
import com.example.levelupgamer.data.local.ProductEntity

class CatalogRepository(
    private val catDao: CategoryDao,
    private val prodDao: ProductDao
) {
    suspend fun getCategories() = catDao.getAll()
    suspend fun getProductsByCategory(slug: String): List<ProductEntity> =
        prodDao.getByCategory(slug)
}
