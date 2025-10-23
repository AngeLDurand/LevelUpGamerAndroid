package com.example.levelupgamer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamer.ui.AppScaffold
import com.example.levelupgamer.ui.BottomItem
import com.example.levelupgamer.ui.theme.SurfaceDark
import com.example.levelupgamer.viewmodel.CategoriesViewModel
import com.example.levelupgamer.viewmodel.CategoryUi

@Composable
fun CategoriesScreen(
    cartCount: Int,
    onCartClick: () -> Unit = {},
    onSelectTab: (BottomItem) -> Unit,
    onCategoryClick: (slug: String) -> Unit
) {
    val vm: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory)
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
                    "Categorías",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                CategoriesSection(
                    items = items,
                    onClick = { onCategoryClick(it.slug) }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun CategoriesSection(
    items: List<CategoryUi>,
    onClick: (CategoryUi) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, cs.onSurface.copy(alpha = 0.06f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(vertical = 4.dp)) {
            items.forEachIndexed { index, c ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(c) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(c.nombre, style = MaterialTheme.typography.bodyLarge, color = cs.onSurface)
                    Icon(
                        Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = cs.onSurfaceVariant
                    )
                }
                if (index != items.lastIndex) {
                    Divider(color = cs.onSurface.copy(alpha = 0.08f))
                }
            }
        }
    }
}
