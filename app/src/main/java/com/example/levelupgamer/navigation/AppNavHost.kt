package com.example.levelupgamer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelupgamer.data.local.AppDatabase
import com.example.levelupgamer.data.local.CartLocal
import com.example.levelupgamer.repository.CartRepository
import com.example.levelupgamer.screens.*
import com.example.levelupgamer.ui.BottomItem
import com.example.levelupgamer.viewmodel.CartViewModel

@Composable
fun AppNavHost(navController: NavHostController) {

    val actions = remember(navController) { NavActions(navController) }


    val ctx = LocalContext.current
    val db = remember { AppDatabase.getInstance(ctx) }
    val cartRepo = remember {
        CartRepository(
            cartLocal = CartLocal(appContext = ctx),
            productDao = db.productDao(),
            compraDao = db.compraDao()
        )
    }
    val cartVm = remember { CartViewModel(cartRepo) }
    val cartState by cartVm.uiState.collectAsState()


    val session = remember { com.example.levelupgamer.data.session.SessionPrefs(ctx) }
    val currentEmail by session.currentEmail.collectAsState(initial = null)

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Welcome.route
    ) {

        /* ---------- Onboarding / Auth ---------- */

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

        /* ---------- Sección principal con bottom bar ---------- */

        composable(NavRoutes.Home.route) {
            HomeScreen(
                cartCount = cartState.totalQty,
                onCartClick = { navController.navigate(NavRoutes.Cart.route) },
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
                cartCount = cartState.totalQty,
                onCartClick = { navController.navigate(NavRoutes.Cart.route) },
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
                cartCount = cartState.totalQty,
                onCartClick = { navController.navigate(NavRoutes.Cart.route) },
                onSelectTab = {
                    when (it) {
                        BottomItem.HOME -> actions.goToHome()
                        BottomItem.CATEGORIES -> actions.goToCategories()
                        BottomItem.ACCOUNT -> actions.goToAccount()
                    }
                },
                onAddToCart = { id -> cartVm.add(id) }
            )
        }

        composable(NavRoutes.Account.route) {
            AccountScreen(
                cartCount = cartState.totalQty,
                onCartClick = { navController.navigate(NavRoutes.Cart.route) },
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

        /* ---------- Carrito y Checkout ---------- */

        composable(NavRoutes.Cart.route) {
            CartScreenNoScaffold(
                viewModel = cartVm,
                onBack = actions.goBack,
                onCheckout = {
                    cartVm.checkout(currentEmail) { purchaseId ->
                        if (purchaseId > 0) {
                            navController.navigate(NavRoutes.PurchaseSuccess.route) {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }

        composable(NavRoutes.PurchaseSuccess.route) {
            PurchaseSuccessScreen(navController = navController)
        }
    }
}
