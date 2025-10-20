package com.example.levelupgamer.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.levelupgamer.viewmodel.UsuarioViewModel

@Composable
fun ResumenScreen(vm: UsuarioViewModel) {
    val ui by vm.ui.collectAsState()

    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            Text("Resumen de registro", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))
            Text("Nombre", style = MaterialTheme.typography.labelLarge)
            Text(ui.nombre.ifBlank { "—" }, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(12.dp))
            Text("Correo", style = MaterialTheme.typography.labelLarge)
            Text(ui.email.ifBlank { "—" }, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
