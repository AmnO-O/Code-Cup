package com.example.codecup.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

@Composable
fun ConfettiEffect(modifier: Modifier = Modifier) {
    val duration = 3000
    val confettiCount = 50
    
    val colors = listOf(
        Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0),
        Color(0xFF673AB7), Color(0xFF3F51B5), Color(0xFF2196F3),
        Color(0xFF03A9F4), Color(0xFF00BCD4), Color(0xFF009688),
        Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
        Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800),
        Color(0xFFFF5722)
    )

    val confettiList = remember {
        List(confettiCount) {
            ConfettiState(
                color = colors.random(),
                xStart = Random.nextFloat(),
                yStart = Random.nextFloat() * -0.5f,
                speed = Random.nextFloat() * 1.5f + 0.5f,
                angle = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 5f + 2f,
                size = Random.nextFloat() * 10f + 10f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        confettiList.forEach { confetti ->
            val y = (confetti.yStart + progress * confetti.speed) * height
            val x = confetti.xStart * width
            
            if (y < height) {
                rotate(confetti.angle + progress * 360 * confetti.rotationSpeed, pivot = Offset(x, y)) {
                    drawRect(
                        color = confetti.color,
                        topLeft = Offset(x, y),
                        size = androidx.compose.ui.geometry.Size(confetti.size, confetti.size / 2)
                    )
                }
            }
        }
    }
}

data class ConfettiState(
    val color: Color,
    val xStart: Float,
    val yStart: Float,
    val speed: Float,
    val angle: Float,
    val rotationSpeed: Float,
    val size: Float
)
