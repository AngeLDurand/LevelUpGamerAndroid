package com.example.levelupgamer.navigation

sealed class NavRoutes(val route: String) {
    data object Welcome    : NavRoutes("welcome")
    data object Register   : NavRoutes("register")
    data object Login      : NavRoutes("login")
    data object Home       : NavRoutes("home")
    data object Categories : NavRoutes("categories")
    data object Account    : NavRoutes("account")
    data object ProductsByCategory : NavRoutes("products/{slug}") {
        fun build(slug: String) = "products/$slug"
    }
}
