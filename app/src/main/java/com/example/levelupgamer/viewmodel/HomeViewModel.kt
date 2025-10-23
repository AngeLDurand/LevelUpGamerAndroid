package com.example.levelupgamer.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.levelupgamer.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FeaturedUi(
    val id: Int,
    val titulo: String,
    val precioClp: Int,
    val imagen: String
)

class HomeViewModel(private val db: AppDatabase) : ViewModel() {
    private val _featured = MutableStateFlow<List<FeaturedUi>>(emptyList())
    val featured: StateFlow<List<FeaturedUi>> = _featured

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val all = db.productDao().getAllDesc()
            _featured.value = all.map {
                FeaturedUi(
                    id = it.id,
                    titulo = it.titulo,
                    precioClp = it.precioClp,
                    imagen = it.thumbUrl.ifBlank { it.imagenUrl }
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = checkNotNull(
                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
                ) { "Application missing" }

                throw IllegalStateException("Usa el invoke abajo")
            }
        }


        fun Factory(app: Application): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                    val db = AppDatabase.getInstance(application)
                    HomeViewModel(db)
                }
            }
    }
}
