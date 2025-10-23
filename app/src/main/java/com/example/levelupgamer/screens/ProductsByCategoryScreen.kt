package com.example.levelupgamer.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamer.R
import com.example.levelupgamer.ui.AppScaffold
import com.example.levelupgamer.ui.BottomItem
import com.example.levelupgamer.ui.theme.BrandYellow
import com.example.levelupgamer.ui.theme.SurfaceDark
import com.example.levelupgamer.viewmodel.ProductUi
import com.example.levelupgamer.viewmodel.ProductsViewModel

@Composable
fun ProductsByCategoryScreen(
    slug: String,
    cartCount: Int,
    onCartClick: () -> Unit = {},
    onSelectTab: (BottomItem) -> Unit,
    onAddToCart: (Int) -> Unit
) {
    val vm: ProductsViewModel = viewModel(factory = ProductsViewModel.Factory(slug))
    val items by vm.items.collectAsState()

    AppScaffold(
        selected = BottomItem.CATEGORIES,
        onSelect = onSelectTab,
        onCartClick = onCartClick,
        cartCount = cartCount
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Productos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(items, key = { it.id }) { p ->
                ProductCard(p, onAddToCart)
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ProductCard(
    p: ProductUi,
    onAddToCart: (Int) -> Unit
) {
    val ctx = LocalContext.current

    // Buscar el drawable por nombre (sin extensión)
    val resId = remember(p.imagenUrl) {
        val id = ctx.resources.getIdentifier(p.imagenUrl, "drawable", ctx.packageName)
        Log.d("IMG", "lookup '${p.imagenUrl}' -> resId=$id")
        if (id != 0) id else R.drawable.ic_launcher_foreground
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
    ) {
        Column(Modifier.padding(16.dp)) {

            // Imagen 16:9 con esquinas
            Surface(
                color = Color.Black,
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 0.dp
            ) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = p.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = p.titulo,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = formatClp(p.precioClp),
                style = MaterialTheme.typography.titleMedium,
                color = BrandYellow
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { onAddToCart(p.id) },   // ← agrega al carrito
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandYellow,
                    contentColor = SurfaceDark
                )
            ) {
                Text("Agregar al carrito")
            }
        }
    }
}

private fun formatClp(v: Int): String = "$" + "%,d".format(v).replace(',', '.')
