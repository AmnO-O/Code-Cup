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
fun ConfettiEffect(
    modifier: Modifier = Modifier,
    trigger: Boolean = false,
    onAnimationEnd: () -> Unit = {}
) {
    if (!trigger) return

    val duration = 2500
    val confettiCount = 60
    
    val colors = listOf(
        Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0),
        Color(0xFF673AB7), Color(0xFF3F51B5), Color(0xFF2196F3),
        Color(0xFFA53C1B), Color(0xFFBE927F), Color(0xFF31170B),
        Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39)
    )

    val confettiList = remember(trigger) {
        List(confettiCount) {
            ConfettiState(
                color = colors.random(),
                xStart = 0.5f, // Start from middle
                yStart = 0.8f, // Start from bottom
                vx = (Random.nextFloat() - 0.5f) * 2f,
                vy = -(Random.nextFloat() * 2f + 1f),
                angle = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 10f - 5f,
                size = Random.nextFloat() * 8f + 8f
            )
        }
    }

    val animatable = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger) {
            animatable.snapTo(0f)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(duration, easing = LinearOutSlowInEasing)
            )
            onAnimationEnd()
        }
    }

    val progress = animatable.value

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        confettiList.forEach { confetti ->
            // Simple physics: x = x0 + vx*t, y = y0 + vy*t + 0.5*g*t^2
            val t = progress
            val gravity = 3f
            
            val x = (confetti.xStart + confetti.vx * t) * width
            val y = (confetti.yStart + confetti.vy * t + 0.5f * gravity * t * t) * height
            
            if (y > 0 && y < height && x > 0 && x < width) {
                rotate(confetti.angle + progress * 720 * confetti.rotationSpeed, pivot = Offset(x, y)) {
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
    val vx: Float,
    val vy: Float,
    val angle: Float,
    val rotationSpeed: Float,
    val size: Float
)
