package com.example.levelupgamer.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class BottomItem { HOME, CATEGORIES, ACCOUNT }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    companyName: String = "LEVEL·UP GAMER",
    selected: BottomItem,
    onSelect: (BottomItem) -> Unit,
    onCartClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(companyName) },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Carrito"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selected == BottomItem.HOME,
                    onClick = { onSelect(BottomItem.HOME) },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = selected == BottomItem.CATEGORIES,
                    onClick = { onSelect(BottomItem.CATEGORIES) },
                    icon = { Icon(Icons.Outlined.Category, contentDescription = "Categorías") },
                    label = { Text("Categorías") }
                )
                NavigationBarItem(
                    selected = selected == BottomItem.ACCOUNT,
                    onClick = { onSelect(BottomItem.ACCOUNT) },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Mi cuenta") },
                    label = { Text("Mi cuenta") }
                )
            }
        },
        content = { inner -> Surface(Modifier.then(Modifier).padding(inner)) { content() } }
    )
}
