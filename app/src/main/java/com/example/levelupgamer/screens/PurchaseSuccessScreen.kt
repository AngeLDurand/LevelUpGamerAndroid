package com.example.levelupgamer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelupgamer.navigation.NavRoutes
import com.example.levelupgamer.ui.theme.BrandYellow
import com.example.levelupgamer.ui.theme.SurfaceDark


@Composable
fun PurchaseSuccessScreen(navController: NavController) {
    val cs = MaterialTheme.colorScheme
    Surface(Modifier.fillMaxSize(), color = cs.background) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡Compra exitosa!", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("Gracias por tu compra.", color = cs.outline)
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {

                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandYellow, contentColor = SurfaceDark
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Volver al inicio")
            }
        }
    }
}


