package com.example.carparkingsystem.ui.theme.screens.splashScreen


import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carparkingsystem.navigation.ROUTE_REGISTER
import com.example.carparkingsystem.navigation.ROUTE_SPLASH
import kotlinx.coroutines.delay

private val BgDeep      = Color(0xFF0F0C29)
private val Purple      = Color(0xFF7F5AF0)
private val PurpleLight = Color(0xFFAB8DF8)
private val Green       = Color(0xFF2CB67D)


@Composable
fun SplashScreen(navController: NavController) {

    // 1. Scale animation — icon grows from 0 to full size
    val scale = remember { Animatable(0f) }

    // 2. Alpha animation — text fades in
    val alpha = remember { Animatable(0f) }

    // 3. Dot loading animation
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    val dot1 = infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(0)
        ), label = "dot1"
    )
    val dot2 = infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(200)
        ), label = "dot2"
    )
    val dot3 = infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(400)
        ), label = "dot3"
    )

    LaunchedEffect(Unit) {
        // Icon bounces in
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness    = Spring.StiffnessLow
            )
        )
        // Text fades in
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
        // Wait then navigate
        delay(2000)
        navController.navigate(ROUTE_REGISTER) {
            popUpTo(ROUTE_SPLASH) { inclusive = true }
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Car icon with scale animation
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(scale.value)
                    .background(
                        Brush.linearGradient(
                            listOf(Purple.copy(alpha = 0.4f), Green.copy(alpha = 0.3f)),
                            start = Offset(0f, 0f),
                            end   = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.DirectionsCar,
                    contentDescription = "Car",
                    modifier           = Modifier.size(72.dp),
                    tint               = PurpleLight
                )

            }

            Spacer(Modifier.height(28.dp))


            Text(
                text  = "DISCRETION CAR PARK",
                fontSize   = 26.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    brush = Brush.horizontalGradient(listOf(Purple, Green)),
                    alpha = alpha.value
                )
            )

            Spacer(Modifier.height(8.dp))

            // Subtitle
            Text(
                text  = "Smart. Fast. Reliable.",
                fontSize = 14.sp,
                color = Color(0xFF94A3B8).copy(alpha = alpha.value)
            )

            Spacer(Modifier.height(48.dp))

            // Animated dots
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(dot1.value, dot2.value, dot3.value).forEach { dotAlpha ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                Purple.copy(alpha = dotAlpha),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}