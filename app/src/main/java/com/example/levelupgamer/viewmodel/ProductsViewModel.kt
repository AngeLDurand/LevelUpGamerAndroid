package com.example.levelupgamer.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.levelupgamer.data.local.AppDatabase
import com.example.levelupgamer.repository.CatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProductUi(
    val id: Int,
    val titulo: String,
    val precioClp: Int,
    val imagenUrl: String,
    val thumbUrl: String
)

class ProductsViewModel(
    private val repo: CatalogRepository,
    private val slug: String
) : ViewModel() {

    private val _items = MutableStateFlow<List<ProductUi>>(emptyList())
    val items: StateFlow<List<ProductUi>> = _items

    init {
        viewModelScope.launch {
            val res = repo.getProductsByCategory(slug).map {
                ProductUi(it.id, it.titulo, it.precioClp, it.imagenUrl, it.thumbUrl)
            }
            _items.value = res
        }
    }

    companion object {
        /** Factory con parámetro 'slug' */
        fun Factory(slug: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                val db = AppDatabase.getInstance(app)
                val repo = CatalogRepository(db.categoryDao(), db.productDao())
                ProductsViewModel(repo, slug)
            }
        }
    }
}
