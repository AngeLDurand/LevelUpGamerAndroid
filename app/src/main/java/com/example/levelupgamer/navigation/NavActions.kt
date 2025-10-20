package com.example.levelupgamer.navigation

import androidx.navigation.NavHostController

class NavActions(private val navController: NavHostController) {
    val goToWelcome: () -> Unit = { navController.navigate(NavRoutes.Welcome.route) }
    val goToRegister: () -> Unit = { navController.navigate(NavRoutes.Register.route) }
    val goToLogin: () -> Unit = { navController.navigate(NavRoutes.Login.route) }
    val goToHome: () -> Unit = { navController.navigate(NavRoutes.Home.route) }
    val goToCategories: () -> Unit = { navController.navigate(NavRoutes.Categories.route) }
    val goToAccount: () -> Unit = { navController.navigate(NavRoutes.Account.route) }
    val goBack: () -> Unit = { navController.popBackStack() }
}
