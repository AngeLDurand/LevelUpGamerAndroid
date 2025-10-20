package com.example.levelupgamer.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.levelupgamer.data.local.AppDatabase
import com.example.levelupgamer.data.session.SessionPrefs
import com.example.levelupgamer.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CompraUi(val id: Int, val titulo: String, val fecha: String, val total: String)

data class AccountUiState(
    val nombre: String = "—",
    val email: String = "—",
    val fotoPerfil: Uri? = null,
    val compras: List<CompraUi> = emptyList(),
    val ready: Boolean = false
)

class AccountViewModel(
    private val repo: UsuarioRepository,
    private val prefs: SessionPrefs
) : ViewModel() {

    private val _ui = MutableStateFlow(AccountUiState())
    val ui: StateFlow<AccountUiState> = _ui

    init {
        // Observa email en sesión y carga el usuario
        viewModelScope.launch {
            prefs.currentEmail.collect { email ->
                if (email != null) {
                    val u = repo.login(email, pass = "___")

                    val usuario = repo.getByEmail(email)
                    _ui.update {
                        it.copy(
                            nombre = usuario?.nombre ?: "—",
                            email = usuario?.email ?: "—",
                            compras = demoCompras(),
                            ready = true
                        )
                    }
                } else {
                    _ui.update { it.copy(ready = true) }
                }
            }
        }
    }

    fun setFoto(uri: Uri?) = _ui.update { it.copy(fotoPerfil = uri) }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            prefs.clear()
            onDone()
        }
    }

    private fun demoCompras() = listOf(
        CompraUi(1, "Auriculares HyperX Cloud II", "10/09/2025", "$79.990"),
        CompraUi(2, "Mouse Logitech G502", "21/08/2025", "$49.990")
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                val db = AppDatabase.getInstance(app)
                val repo = UsuarioRepository(db.usuarioDao())
                val prefs = SessionPrefs(app)
                AccountViewModel(repo, prefs)
            }
        }
    }
}
