package com.example.levelupgamer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelupgamer.screens.*
import com.example.levelupgamer.ui.BottomItem

@Composable
fun AppNavHost(navController: NavHostController) {
    val actions = remember(navController) { NavActions(navController) }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Welcome.route
    ) {
        composable(NavRoutes.Welcome.route) {
            WelcomeScreen(
                onCreateAccountClick = actions.goToRegister,
                onLoginClick = actions.goToLogin
            )
        }

        composable(NavRoutes.Register.route) {
            RegistroScreen(
                onBack = actions.goBack,
                onRegistered = actions.goToLogin
            )
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(
                onBack = actions.goBack,
                onLoggedIn = actions.goToHome
            )
        }

        // --- sección principal con bottom bar ---
        composable(NavRoutes.Home.route) {
            HomeScreen(
                onCartClick = { /* TODO: ir a carrito */ },
                onSelectTab = {
                    when (it) {
                        BottomItem.HOME -> actions.goToHome()
                        BottomItem.CATEGORIES -> actions.goToCategories()
                        BottomItem.ACCOUNT -> actions.goToAccount()
                    }
                }
            )
        }

        composable(NavRoutes.Categories.route) {
            CategoriesScreen(
                onCartClick = { /* TODO */ },
                onSelectTab = {
                    when (it) {
                        BottomItem.HOME -> actions.goToHome()
                        BottomItem.CATEGORIES -> actions.goToCategories()
                        BottomItem.ACCOUNT -> actions.goToAccount()
                    }
                },
                onCategoryClick = { slug ->
                    navController.navigate(NavRoutes.ProductsByCategory.build(slug))
                }
            )
        }


        composable(NavRoutes.ProductsByCategory.route) { backStack ->
            val slug = backStack.arguments?.getString("slug") ?: "computacion"
            ProductsByCategoryScreen(
                slug = slug,
                onCartClick = { /* TODO */ },
                onSelectTab = {
                    when (it) {
                        BottomItem.HOME -> actions.goToHome()
                        BottomItem.CATEGORIES -> actions.goToCategories()
                        BottomItem.ACCOUNT -> actions.goToAccount()
                    }
                }
            )
        }

        composable(NavRoutes.Account.route) {
            AccountScreen(
                onCartClick = { /* TODO: ir a carrito */ },
                onSelectTab = {
                    when (it) {
                        BottomItem.HOME -> actions.goToHome()
                        BottomItem.CATEGORIES -> actions.goToCategories()
                        BottomItem.ACCOUNT -> actions.goToAccount()
                    }
                },
                onLogout = actions.goToWelcome
            )
        }
    }
}
