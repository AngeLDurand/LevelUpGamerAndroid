package com.example.levelupgamer.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamer.ui.AppScaffold
import com.example.levelupgamer.ui.BottomItem
import com.example.levelupgamer.viewmodel.CategoriesViewModel
import com.example.levelupgamer.viewmodel.CategoryUi

@Composable
fun CategoriesScreen(
    onCartClick: () -> Unit = {},
    onSelectTab: (BottomItem) -> Unit,
    onCategoryClick: (slug: String) -> Unit
) {
    val vm: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory)
    val items by vm.items.collectAsState()

    AppScaffold(
        selected = BottomItem.CATEGORIES,
        onSelect = onSelectTab,
        onCartClick = onCartClick
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            // Header
            item {
                Text(
                    "Categorías",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
            }
            // Items
            items(items) { c ->
                CategoryRow(c) { onCategoryClick(c.slug) }
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun CategoryRow(c: CategoryUi, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(c.nombre, style = MaterialTheme.typography.bodyLarge)
        Icon(Icons.Outlined.ChevronRight, contentDescription = null)
    }
}
