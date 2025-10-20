package com.example.levelupgamer.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamer.R
import com.example.levelupgamer.ui.AppScaffold
import com.example.levelupgamer.ui.BottomItem
import com.example.levelupgamer.viewmodel.ProductUi
import com.example.levelupgamer.viewmodel.ProductsViewModel

@Composable
fun ProductsByCategoryScreen(
    slug: String,
    onCartClick: () -> Unit = {},
    onSelectTab: (BottomItem) -> Unit
) {
    val vm: ProductsViewModel = viewModel(factory = ProductsViewModel.Factory(slug))
    val items by vm.items.collectAsState()

    AppScaffold(
        selected = BottomItem.CATEGORIES,
        onSelect = onSelectTab,
        onCartClick = onCartClick
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Productos",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(items, key = { it.id }) { p ->
                ProductCard(p)
            }
        }
    }
}

@Composable
private fun ProductCard(p: ProductUi) {
    val context = LocalContext.current

    // Buscar el drawable por nombre (sin extensión)
    val resId = remember(p.imagenUrl) {
        val id = context.resources.getIdentifier(p.imagenUrl, "drawable", context.packageName)
        Log.d("IMG", "lookup '${p.imagenUrl}' -> resId=$id")
        if (id != 0) id else R.drawable.ic_launcher_foreground // fallback local
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // 👈 tono más claro
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = p.titulo,
                modifier = Modifier
                    .width(303.dp)
                    .height(227.25.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .align(Alignment.CenterHorizontally)

            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = p.titulo,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(6.dp))


            Text(
                text = formatClp(p.precioClp),
                style = MaterialTheme.typography.headlineSmall, // más grande
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { /* TODO: agregar al carrito */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,  // tu amarillo
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Agregar al carrito")
            }

            Spacer(Modifier.height(4.dp))
            HorizontalDivider()
        }
    }
}

private fun formatClp(v: Int): String = "$" + "%,d".format(v).replace(',', '.')
