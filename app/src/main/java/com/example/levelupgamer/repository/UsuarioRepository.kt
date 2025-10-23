package com.example.levelupgamer.repository

import com.example.levelupgamer.data.local.UsuarioDao
import com.example.levelupgamer.data.local.UsuarioEntity

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun registrar(nombre: String, email: String, pass: String) {
        val exists = dao.findByEmail(email)
        if (exists != null) throw IllegalStateException("El correo ya está registrado")
        dao.insert(UsuarioEntity(nombre = nombre, email = email, pass = pass))
    }


    suspend fun login(email: String, pass: String): UsuarioEntity? {
        val u = dao.findByEmail(email) ?: return null
        return if (u.pass == pass) u else null
    }

    suspend fun getByEmail(email: String) = dao.findByEmail(email)

}
