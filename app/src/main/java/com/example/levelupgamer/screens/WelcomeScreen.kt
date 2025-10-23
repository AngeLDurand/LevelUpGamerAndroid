package com.example.levelupgamer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.levelupgamer.R
import com.example.levelupgamer.ui.theme.BrandYellow
import com.example.levelupgamer.ui.theme.SurfaceDark

@Composable
fun WelcomeScreen(
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize()) {
        // 1) Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = "Imagen de bienvenida",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2) Velo oscuro general para legibilidad
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.65f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // 3) Capa BLUR sólo en la parte inferior (donde va la card)

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .heightIn(min = 320.dp, max = 420.dp) // alto de la zona difuminada
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .blur(22.dp),
                contentScale = ContentScale.Crop
            )
            // Velo extra para contraste
            Box(
                Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            )
        }

        // 4) Card al fondo, sobre la zona con blur
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 40.dp)
                .widthIn(max = 480.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(4.dp),
            border = BorderStroke(1.dp, cs.onSurface.copy(alpha = 0.06f))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "LEVEL·UP GAMER",
                    color = BrandYellow,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Bienvenido 🎮",
                    style = MaterialTheme.typography.headlineSmall,
                    color = cs.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Equipa tu setup con lo mejor en componentes, periféricos y streaming.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurface.copy(alpha = 0.78f),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onCreateAccountClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandYellow,
                        contentColor = SurfaceDark
                    )
                ) {
                    Text("Crear cuenta")
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BrandYellow
                    ),
                    border = BorderStroke(1.dp, BrandYellow.copy(alpha = 0.8f))
                ) {
                    Text("Iniciar sesión")
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Al continuar aceptas nuestra política de privacidad y términos.",
                    style = MaterialTheme.typography.labelSmall,
                    color = cs.onSurface.copy(alpha = 0.55f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
