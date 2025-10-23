package com.example.levelupgamer.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.levelupgamer.R
import com.example.levelupgamer.ui.theme.BrandYellow
import com.example.levelupgamer.ui.theme.SurfaceDark
import com.example.levelupgamer.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenNoScaffold(
    viewModel: CartViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val cs = MaterialTheme.colorScheme

    // Fondo negro (background del tema)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = cs.background
    ) {
        Column(Modifier.fillMaxSize()) {

            // Top bar propia, estilo oscuro
            TopAppBar(
                title = { Text("Carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (!state.isEmpty) {
                        TextButton(onClick = { viewModel.clear() }) {
                            Text("Vaciar", color = BrandYellow)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceDark,                  // barra oscura
                    titleContentColor = cs.onSurface,
                    navigationIconContentColor = cs.onSurface,
                    actionIconContentColor = cs.onSurface
                )
            )

            Spacer(Modifier.height(8.dp))

            if (state.isEmpty) {
                EmptyCart()  // mensaje centrado
            } else {
                // Lista con padding similar al AppScaffold (16h/12v)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.items, key = { it.id }) { item ->
                        CartItemCard(
                            item = item,
                            onInc = { viewModel.inc(item.id) },
                            onDec = { viewModel.dec(item.id) },
                            onRemove = { viewModel.remove(item.id) }
                        )
                    }

                    item {
                        TotalCard(totalQty = state.totalQty, totalClp = state.totalClp)
                    }
                }

                // Botón Finalizar compra al pie, con el mismo padding lateral
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Button(
                        onClick = onCheckout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandYellow,
                            contentColor = SurfaceDark
                        )
                    ) {
                        Text("Finalizar compra")
                    }
                }
            }
        }
    }
}

/* ---------------- Subcomponentes ---------------- */

@Composable
private fun CartItemCard(
    item: com.example.levelupgamer.model.CartItemUi,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val ctx = LocalContext.current
    val imgRes = remember(item.image) {
        val id = ctx.resources.getIdentifier(item.image, "drawable", ctx.packageName)
        Log.d("IMG", "lookup '${item.image}' -> resId=$id")
        if (id != 0) id else R.drawable.ic_launcher_foreground
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),  // mismo tono que AccountScreen
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, cs.onSurface.copy(alpha = 0.06f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = imgRes),
                    contentDescription = item.title,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(item.title, style = MaterialTheme.typography.bodyLarge, color = cs.onSurface)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "x${item.qty} · ${clp(item.priceClp)} = ${clp(item.lineTotalClp)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.outline
                    )
                }
                IconButton(onClick = onRemove) { Icon(Icons.Filled.Delete, contentDescription = "Quitar") }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedIconButton(onClick = onDec, enabled = item.qty > 1) {
                    Icon(Icons.Filled.Remove, contentDescription = "Menos")
                }
                Text(
                    "${item.qty}",
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurface
                )
                OutlinedIconButton(onClick = onInc) {
                    Icon(Icons.Filled.Add, contentDescription = "Más")
                }
            }
        }
    }
}

@Composable
private fun TotalCard(totalQty: Int, totalClp: Int) {
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, cs.onSurface.copy(alpha = 0.06f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total (${totalQty} ítems)", style = MaterialTheme.typography.bodyLarge, color = cs.onSurface)
            Text(clp(totalClp), style = MaterialTheme.typography.titleMedium, color = BrandYellow)
        }
    }
}

@Composable
private fun EmptyCart() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
        Text(
            "Explora el catálogo y agrega productos.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

/* ---------------- Utilidad CLP ---------------- */

private val CL_LOCALE = Locale("es", "CL")
private fun clp(value: Int): String {
    val nf = NumberFormat.getCurrencyInstance(CL_LOCALE)
    nf.maximumFractionDigits = 0
    return nf.format(value)
}
