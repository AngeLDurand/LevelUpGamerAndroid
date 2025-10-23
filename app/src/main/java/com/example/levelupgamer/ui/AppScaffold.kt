// ui/AppScaffold.kt
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.levelupgamer.ui.theme.SurfaceDark
import com.example.levelupgamer.ui.theme.BrandYellow

enum class BottomItem { HOME, CATEGORIES, ACCOUNT }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    companyName: String = "LEVEL·UP GAMER",
    selected: BottomItem,
    onSelect: (BottomItem) -> Unit,
    onCartClick: () -> Unit,
    cartCount: Int = 0,
    content: @Composable () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Scaffold(
        containerColor = cs.background,
        topBar = {
            TopAppBar(
                title = { Text(companyName) },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge { Text(cartCount.coerceAtMost(99).toString()) }
                            }
                        }
                    ) {
                        IconButton(onClick = onCartClick) {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceDark,
                    titleContentColor = BrandYellow,
                    actionIconContentColor = cs.onBackground
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = SurfaceDark, tonalElevation = 0.dp) {
                NavigationBarItem(
                    selected = selected == BottomItem.HOME,
                    onClick = { onSelect(BottomItem.HOME) },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SurfaceDark,
                        selectedTextColor = BrandYellow,
                        unselectedIconColor = Color(0xFFECECEC),
                        unselectedTextColor = Color(0xFFECECEC),
                        indicatorColor = BrandYellow
                    )
                )
                NavigationBarItem(
                    selected = selected == BottomItem.CATEGORIES,
                    onClick = { onSelect(BottomItem.CATEGORIES) },
                    icon = { Icon(Icons.Outlined.Category, contentDescription = "Categorías") },
                    label = { Text("Categorías") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SurfaceDark,
                        selectedTextColor = BrandYellow,
                        unselectedIconColor = Color(0xFFECECEC),
                        unselectedTextColor = Color(0xFFECECEC),
                        indicatorColor = BrandYellow
                    )
                )
                NavigationBarItem(
                    selected = selected == BottomItem.ACCOUNT,
                    onClick = { onSelect(BottomItem.ACCOUNT) },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Mi cuenta") },
                    label = { Text("Mi cuenta") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SurfaceDark,
                        selectedTextColor = BrandYellow,
                        unselectedIconColor = Color(0xFFECECEC),
                        unselectedTextColor = Color(0xFFECECEC),
                        indicatorColor = BrandYellow
                    )
                )
            }
        },
        content = { inner ->
            Surface(
                modifier = Modifier
                    .padding(inner)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                color = Color.Transparent
            ) { content() }
        }
    )
}
