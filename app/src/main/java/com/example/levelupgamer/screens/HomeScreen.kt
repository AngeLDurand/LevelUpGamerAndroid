package com.example.levelupgamer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelupgamer.ui.AppScaffold
import com.example.levelupgamer.ui.BottomItem

@Composable
fun HomeScreen(
    onCartClick: () -> Unit = {},
    onSelectTab: (BottomItem) -> Unit
) {
    AppScaffold(
        selected = BottomItem.HOME,
        onSelect = onSelectTab,
        onCartClick = onCartClick
    ) {
        // Contenido demo
        Column(
            Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Catálogo destacado", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            Text("Aquí irían tus cards de productos…")
        }
    }
}
