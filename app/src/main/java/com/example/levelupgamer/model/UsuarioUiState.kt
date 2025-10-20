package com.example.levelupgamer.model

data class UsuarioUiState(
    val nombre: String = "",
    val email: String = "",
    val pass: String = "",
    val pass2: String = "",
    val errores: UsuarioErrores = UsuarioErrores(),


)
