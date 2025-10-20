package com.example.levelupgamer.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.levelupgamer.ui.AppScaffold
import com.example.levelupgamer.ui.BottomItem
import com.example.levelupgamer.viewmodel.AccountViewModel
import com.example.levelupgamer.viewmodel.CompraUi

@Composable
fun AccountScreen(
    onCartClick: () -> Unit = {},
    onSelectTab: (BottomItem) -> Unit,
    onLogout: () -> Unit = {} // navegar a Welcome
) {
    val vm: AccountViewModel = viewModel(factory = AccountViewModel.Factory)
    val ui by vm.ui.collectAsState()

    // Solo galería (Photo Picker)
    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> vm.setFoto(uri) }

    AppScaffold(
        selected = BottomItem.ACCOUNT,
        onSelect = onSelectTab,
        onCartClick = onCartClick
    ) {
        if (!ui.ready) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@AppScaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Título local de la pantalla
            item {
                Text(
                    "Mi cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))
            }

            // Foto + datos
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileAvatar(
                        photo = ui.fotoPerfil,
                        onClick = {
                            pickPhotoLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(ui.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(ui.email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                        TextButton(onClick = {
                            pickPhotoLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            Icon(Icons.Outlined.Image, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("Cambiar foto")
                        }
                    }
                }
            }

            // Información personal
            item {
                SectionHeader("Información personal")
                ListItem(headlineContent = { Text("Nombre") }, supportingContent = { Text(ui.nombre) })
                ListItem(headlineContent = { Text("Correo") }, supportingContent = { Text(ui.email) })
                Divider()
                Spacer(Modifier.height(8.dp))
            }

            // Compras
            item { SectionHeader("Compras") }
            items(ui.compras, key = { it.id }) { compra ->
                CompraItem(compra)
            }

            // Cerrar sesión
            item {
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { vm.logout(onLogout) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Outlined.Logout, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar sesión")
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ProfileAvatar(photo: android.net.Uri?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(84.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (photo != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo)
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun CompraItem(c: CompraUi) {
    ListItem(
        headlineContent = { Text(c.titulo) },
        supportingContent = { Text("${c.fecha} • Total ${c.total}") }
    )
    Divider()
}
