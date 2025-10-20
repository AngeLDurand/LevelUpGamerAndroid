package com.example.levelupgamer.navigation

import androidx.navigation.NavHostController

class NavActions(private val navController: NavHostController) {

    val goToWelcome: () -> Unit = {
        navController.navigateSingleTop(NavRoutes.Welcome.route)
    }

    val goToRegister: () -> Unit = {
        navController.navigateSingleTop(NavRoutes.Register.route)
    }

    val goToLogin: () -> Unit = {
        navController.navigateSingleTop(NavRoutes.Login.route)
    }

    val goToHome: () -> Unit = {
        navController.navigateSingleTop(NavRoutes.Home.route)
    }

    val goToCategories: () -> Unit = {
        navController.navigateSingleTop(NavRoutes.Categories.route)
    }

    val goToAccount: () -> Unit = {
        navController.navigateSingleTop(NavRoutes.Account.route)
    }

    val goBack: () -> Unit = {
        navController.popBackStack()
    }
}

fun NavHostController.navigateSingleTop(route: String) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) { saveState = true }
    }
}
