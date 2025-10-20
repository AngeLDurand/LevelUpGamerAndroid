package com.example.levelupgamer.viewmodel

import android.app.Application
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

data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val emailErr: String? = null,
    val passErr: String? = null
)

class LoginViewModel(
    private val repo: UsuarioRepository,
    private val prefs: SessionPrefs
) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmail(v: String) = _ui.update { it.copy(email = v, emailErr = null) }
    fun onPass(v: String)  = _ui.update { it.copy(pass = v,  passErr = null) }

    private fun validar(): Boolean {
        val s = _ui.value
        val emailErr = when {
            s.email.isBlank() -> "Ingresa tu correo"
            !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(s.email) -> "Correo inválido"
            else -> null
        }
        val passErr = if (s.pass.isBlank()) "Ingresa tu contraseña" else null
        _ui.update { it.copy(emailErr = emailErr, passErr = passErr) }
        return emailErr == null && passErr == null
    }

    /** Guarda email en DataStore si el login es correcto */
    fun login(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!validar()) return
        val s = _ui.value
        viewModelScope.launch {
            val user = repo.login(s.email.trim().lowercase(), s.pass)
            if (user != null) {
                prefs.setCurrentEmail(user.email)
                onSuccess()
            } else onError("Credenciales inválidas")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                val db = AppDatabase.getInstance(app)
                val repo = UsuarioRepository(db.usuarioDao())
                val prefs = SessionPrefs(app)
                LoginViewModel(repo, prefs)
            }
        }
    }
}
