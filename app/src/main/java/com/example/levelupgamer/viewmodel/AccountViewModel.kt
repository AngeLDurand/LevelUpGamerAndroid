package com.example.levelupgamer.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.local.AppDatabase
import com.example.levelupgamer.data.local.CompraDao
import com.example.levelupgamer.data.local.UsuarioDao
import com.example.levelupgamer.data.session.SessionPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

/* ===== UI models ===== */

data class AccountUi(
    val ready: Boolean = false,
    val nombre: String = "",
    val email: String = "",
    val fotoPerfil: Uri? = null,
    val compras: List<CompraUi> = emptyList()
)

data class CompraUi(
    val titulo: String,
    val fecha: String,
    val total: String
)

/* ===== ViewModel ===== */

class AccountViewModel(
    private val session: SessionPrefs,
    private val usuarioDao: UsuarioDao,
    private val compraDao: CompraDao
) : ViewModel() {

    private val _ui = MutableStateFlow(AccountUi())
    val ui: StateFlow<AccountUi> = _ui

    init { load() }

    private fun load() = viewModelScope.launch {
        val email = session.currentEmail.first().orEmpty()

        val persisted = session.profilePhotoUri.first()
        val currentPhoto = _ui.value.fotoPerfil ?: persisted?.let { Uri.parse(it) }

        val nombre = try {
            usuarioDao.findByEmail(email)?.nombre ?: email.substringBefore("@")
        } catch (_: Exception) {
            email.substringBefore("@")
        }

        val compras = if (email.isBlank()) {
            emptyList()
        } else {
            val df = SimpleDateFormat("dd/MM/yyyy", Locale("es", "CL"))
            val nf = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
                maximumFractionDigits = 0
            }

            compraDao.getComprasByEmail(email).map { compra ->
                val lineas = compraDao.getLineasByCompra(compra.id)
                val titulo = when {
                    lineas.isEmpty() -> "Compra sin líneas"
                    lineas.size == 1 -> lineas.first().title
                    else -> "${lineas.first().title} + ${lineas.size - 1} más"
                }
                val fecha = df.format(compra.fechaMillis)
                val total = nf.format(compra.totalClp) // ej: $ 79.990
                CompraUi(titulo = titulo, fecha = fecha, total = total)
            }
        }

        _ui.value = AccountUi(
            ready = true,
            nombre = nombre,
            email = email,
            fotoPerfil = currentPhoto,
            compras = compras
        )
    }

    /* ===== Acciones UI ===== */

    fun setFoto(uri: Uri?) = viewModelScope.launch {
        _ui.value = _ui.value.copy(fotoPerfil = uri)
        session.setProfilePhoto(uri)
    }


    fun logout(onDone: () -> Unit) = viewModelScope.launch {
        session.clear()
        onDone()
    }

    fun refresh() = viewModelScope.launch { load() }

    /* ===== Factory para inyección sencilla en Compose ===== */

    companion object {
        fun provideFactory(
            db: AppDatabase,
            session: SessionPrefs
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AccountViewModel(
                    session = session,
                    usuarioDao = db.usuarioDao(),
                    compraDao = db.compraDao()
                ) as T
            }
        }
    }
}
