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

data class CategoryUi(val slug: String, val nombre: String)

class CategoriesViewModel(private val repo: CatalogRepository) : ViewModel() {
    private val _items = MutableStateFlow<List<CategoryUi>>(emptyList())
    val items: StateFlow<List<CategoryUi>> = _items

    init {
        viewModelScope.launch {
            _items.value = repo.getCategories().map { CategoryUi(it.slug, it.nombre) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                val db = AppDatabase.getInstance(app)
                CategoriesViewModel(CatalogRepository(db.categoryDao(), db.productDao()))
            }
        }
    }
}
