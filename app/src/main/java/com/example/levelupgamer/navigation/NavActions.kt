package com.example.levelupgamer.navigation

import androidx.navigation.NavHostController

class NavActions(private val navController: NavHostController) {

    val goToWelcome: () -> Unit = {

        navController.navigate(NavRoutes.Welcome.route) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
            launchSingleTop = true
        }
    }

    val goToRegister: () -> Unit = {
        navController.navigateSingleTop(NavRoutes.Register.route)
    }

    val goToLogin: () -> Unit = {
        navController.navigateSingleTop(NavRoutes.Login.route)
    }

    val goToHome: () -> Unit = {
        navController.navigateSingleTopRestore(
            route = NavRoutes.Home.route,
            popUpToRoute = NavRoutes.Home.route
        )
    }

    val goToCategories: () -> Unit = {

        navController.navigateSingleTopNoRestore(
            route = NavRoutes.Categories.route,
            popUpToRoute = NavRoutes.Home.route
        )
    }


    val goToAccount: () -> Unit = {
        navController.navigateSingleTopRestore(
            route = NavRoutes.Account.route,
            popUpToRoute = NavRoutes.Home.route
        )
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


fun NavHostController.navigateSingleTopRestore(route: String, popUpToRoute: String) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(popUpToRoute) { saveState = true }
    }
}


fun NavHostController.navigateSingleTopNoRestore(route: String, popUpToRoute: String) {
    this.navigate(route) {
        launchSingleTop = true

        popUpTo(popUpToRoute) { saveState = true }
    }
}
