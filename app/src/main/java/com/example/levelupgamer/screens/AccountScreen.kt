package com.example.levelupgamer.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.levelupgamer.ui.AppScaffold
import com.example.levelupgamer.ui.BottomItem
import com.example.levelupgamer.ui.theme.BrandYellow
import com.example.levelupgamer.ui.theme.SurfaceDark
import com.example.levelupgamer.viewmodel.AccountViewModel
import com.example.levelupgamer.viewmodel.CompraUi

@Composable
fun AccountScreen(
    onCartClick: () -> Unit = {},
    onSelectTab: (BottomItem) -> Unit,
    onLogout: () -> Unit = {}
) {
    val vm: AccountViewModel = viewModel(factory = AccountViewModel.Factory)
    val ui by vm.ui.collectAsState()

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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            item {
                Text(
                    "Mi cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Card de Perfil (avatar + nombre + email + cambiar foto)
            item {
                ProfileCard(
                    nombre = ui.nombre,
                    email = ui.email,
                    photo = ui.fotoPerfil,
                    onChangePhoto = {
                        pickPhotoLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }

            // Card 1: Información personal
            item {
                PersonalInfoCard(
                    nombre = ui.nombre,
                    email = ui.email
                )
            }

            // Card 2: Compras
            item {
                PurchasesCard(compras = ui.compras)
            }

            // Botón cerrar sesión
            item {
                Button(
                    onClick = { vm.logout(onLogout) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandYellow,
                        contentColor = SurfaceDark
                    )
                ) {
                    Icon(Icons.Outlined.Logout, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar sesión")
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* =====================  Cards  ===================== */

@Composable
private fun ProfileCard(
    nombre: String,
    email: String,
    photo: android.net.Uri?,
    onChangePhoto: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAvatar(photo = photo, onClick = onChangePhoto)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                TextButton(onClick = onChangePhoto) {
                    Icon(Icons.Outlined.Image, contentDescription = null, tint = BrandYellow)
                    Spacer(Modifier.width(6.dp))
                    Text("Cambiar foto", color = BrandYellow)
                }
            }
        }
    }
}

@Composable
private fun PersonalInfoCard(
    nombre: String,
    email: String
) {
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, cs.onSurface.copy(alpha = 0.06f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Información personal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = cs.onSurface
            )
            Spacer(Modifier.height(8.dp))
            InfoRow("Nombre", nombre)
            InfoRow("Correo", email)
        }
    }
}

@Composable
private fun PurchasesCard(compras: List<CompraUi>) {
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, cs.onSurface.copy(alpha = 0.06f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Compras",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = cs.onSurface
            )
            Spacer(Modifier.height(8.dp))

            if (compras.isEmpty()) {
                Text("Sin compras registradas", color = cs.outline)
            } else {
                compras.forEachIndexed { i, c ->
                    CompraRow(c)
                    if (i != compras.lastIndex) {
                        HorizontalDivider(color = cs.onSurface.copy(alpha = 0.12f))
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

/* =====================  Subcomponentes  ===================== */

@Composable
private fun ProfileAvatar(photo: android.net.Uri?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(84.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant) // más claro dentro de la card
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
private fun InfoRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun CompraRow(c: CompraUi) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Text(c.titulo, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Text("${c.fecha} • Total ${c.total}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
    }
}
