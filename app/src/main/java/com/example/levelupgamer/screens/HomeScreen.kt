package com.example.levelupgamer.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamer.R
import com.example.levelupgamer.ui.AppScaffold
import com.example.levelupgamer.ui.BottomItem
import com.example.levelupgamer.ui.theme.BrandYellow
import com.example.levelupgamer.ui.theme.SurfaceDark
import com.example.levelupgamer.viewmodel.FeaturedUi
import com.example.levelupgamer.viewmodel.HomeViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    cartCount: Int,
    onCartClick: () -> Unit = {},
    onSelectTab: (BottomItem) -> Unit
) {

    val vm: HomeViewModel = viewModel(factory = HomeViewModel.Factory(LocalContext.current.applicationContext as android.app.Application))
    val featured by vm.featured.collectAsState()

    AppScaffold(
        selected = BottomItem.HOME,
        onSelect = onSelectTab,
        onCartClick = onCartClick,
        cartCount = cartCount
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ====== Cabecera / Hero ======
            HeroHeader(
                title = "Equipa tu setup",
                subtitle = "Componentes, gaming y streaming",
                imageRes = R.drawable.welcome,
                onExploreClick = { onSelectTab(BottomItem.CATEGORIES) }
            )

            // ====== Sección destacados ======
            Text(
                text = "Destacados",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            FeaturedRow(items = featured)
            Spacer(Modifier.height(8.dp))
        }
    }
}

/* ----------------- Subcomponentes ----------------- */

@Composable
private fun HeroHeader(
    title: String,
    subtitle: String,
    imageRes: Int,
    onExploreClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, cs.onSurface.copy(alpha = 0.06f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(12.dp))

            Text(title, style = MaterialTheme.typography.titleMedium, color = cs.onSurface)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = cs.onSurface.copy(alpha = 0.75f))

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onExploreClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandYellow),
                border = BorderStroke(1.dp, BrandYellow.copy(alpha = 0.75f)),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Explorar categorías") }
        }
    }
}

@Composable
private fun FeaturedRow(items: List<FeaturedUi>) {
    val cs = MaterialTheme.colorScheme
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(items, key = { it.id }) { p ->
            val ctx = LocalContext.current
            val imgRes = remember(p.imagen) {
                val id = ctx.resources.getIdentifier(p.imagen, "drawable", ctx.packageName)
                Log.d("IMG", "lookup '${p.imagen}' -> resId=$id")
                if (id != 0) id else R.drawable.ic_launcher_foreground
            }

            Card(
                modifier = Modifier
                    .width(220.dp)
                    .heightIn(min = 220.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                elevation = CardDefaults.cardElevation(0.dp),
                border = BorderStroke(1.dp, cs.onSurface.copy(alpha = 0.06f))
            ) {
                Column(Modifier.padding(12.dp)) {
                    Image(
                        painter = painterResource(id = imgRes),
                        contentDescription = p.titulo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        p.titulo,
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurface,
                        maxLines = 2
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        clp(p.precioClp),
                        style = MaterialTheme.typography.titleMedium,
                        color = BrandYellow,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/* ----------- Utilidad CLP ----------- */
private val CL = Locale("es", "CL")
private fun clp(v: Int): String {
    val nf = NumberFormat.getCurrencyInstance(CL)
    nf.maximumFractionDigits = 0
    return nf.format(v)
}
