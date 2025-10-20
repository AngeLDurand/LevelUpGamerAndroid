package com.example.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import android.app.Application
import com.example.levelupgamer.data.local.AppDatabase
import com.example.levelupgamer.model.UsuarioErrores
import com.example.levelupgamer.model.UsuarioUiState
import com.example.levelupgamer.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repo: UsuarioRepository) : ViewModel() {

    private val _ui = MutableStateFlow(UsuarioUiState())
    val ui: StateFlow<UsuarioUiState> = _ui

    // 👇 indicador de carga
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun onNombre(v: String) = _ui.update {
        it.copy(nombre = v, errores = it.errores.copy(nombre = null))
    }

    fun onEmail(v: String) = _ui.update {
        it.copy(email = v, errores = it.errores.copy(email = null))
    }

    fun onPass(v: String) = _ui.update {
        it.copy(pass = v, errores = it.errores.copy(pass = null))
    }

    fun onPass2(v: String) = _ui.update {
        it.copy(pass2 = v, errores = it.errores.copy(pass2 = null))
    }

    /** Valida campos. Retorna true si todo bien. */
    fun validarFormulario(): Boolean {
        val s = _ui.value

        val nameErr = when {
            s.nombre.isBlank() -> "Ingresa tu nombre"
            s.nombre.trim().length < 2 -> "Mínimo 2 caracteres"
            else -> null
        }

        val emailErr = when {
            s.email.isBlank() -> "Ingresa tu correo"
            !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(s.email) -> "Correo inválido"
            else -> null
        }

        val passErr = when {
            s.pass.length < 8 -> "Mínimo 8 caracteres"
            !Regex("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)").containsMatchIn(s.pass) ->
                "Usa mayúsculas, minúsculas y números"
            else -> null
        }

        val pass2Err = when {
            s.pass2.isBlank() -> "Confirma tu contraseña"
            s.pass2 != s.pass -> "Las contraseñas no coinciden"
            else -> null
        }

        _ui.update { it.copy(errores = UsuarioErrores(nameErr, emailErr, passErr, pass2Err)) }
        return listOf(nameErr, emailErr, passErr, pass2Err).all { it == null }
    }

    /** Valida y guarda. Muestra loading; onSuccess/onError para UI. */
    fun registrar(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!validarFormulario()) return
        val s = _ui.value
        viewModelScope.launch {
            _loading.value = true
            try {
                repo.registrar(
                    nombre = s.nombre.trim(),
                    email = s.email.trim().lowercase(),
                    pass = s.pass
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error al registrar")
            } finally {
                _loading.value = false
            }
        }
    }

    // ---- Factory sin Hilt ----
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                val db = AppDatabase.getInstance(app)
                val repo = UsuarioRepository(db.usuarioDao())
                UsuarioViewModel(repo)
            }
        }
    }
}
