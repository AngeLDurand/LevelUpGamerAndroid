package com.example.levelupgamer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamer.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    vm: UsuarioViewModel = viewModel(factory = UsuarioViewModel.Factory),
    onBack: () -> Unit = {},
    onRegistered: () -> Unit = {}
) {
    val ui by vm.ui.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope() //
    val (showPass, setShowPass) = remember { mutableStateOf(false) }
    val (showPass2, setShowPass2) = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de usuario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nombre
            OutlinedTextField(
                value = ui.nombre,
                onValueChange = vm::onNombre,
                label = { Text("Nombre completo") },
                singleLine = true,
                isError = ui.errores.nombre != null,
                supportingText = { ui.errores.nombre?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            // Email
            OutlinedTextField(
                value = ui.email,
                onValueChange = vm::onEmail,
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = ui.errores.email != null,
                supportingText = { ui.errores.email?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            // Contraseña
            OutlinedTextField(
                value = ui.pass,
                onValueChange = vm::onPass,
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { setShowPass(!showPass) }) {
                        Icon(
                            imageVector = if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (showPass) "Ocultar" else "Mostrar"
                        )
                    }
                },
                isError = ui.errores.pass != null,
                supportingText = {
                    ui.errores.pass?.let { Text(it) }
                        ?: Text("Mínimo 8 caracteres, usa mayúsculas y números.")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

            // Confirmar contraseña
            OutlinedTextField(
                value = ui.pass2,
                onValueChange = vm::onPass2,
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = if (showPass2) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { setShowPass2(!showPass2) }) {
                        Icon(
                            imageVector = if (showPass2) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (showPass2) "Ocultar" else "Mostrar"
                        )
                    }
                },
                isError = ui.errores.pass2 != null,
                supportingText = { ui.errores.pass2?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    vm.registrar(
                        onSuccess = onRegistered,
                        onError = { msg ->
                            // 👇 NO usar LaunchedEffect aquí; estamos fuera de un @Composable
                            scope.launch { snackbar.showSnackbar(message = msg) }
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Crear cuenta")
            }
        }
    }
}
