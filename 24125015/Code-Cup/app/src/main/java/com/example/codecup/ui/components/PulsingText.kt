package com.example.codecup.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle

/**
 * Text that scale-pulses (~200ms total) whenever its content changes — used for
 * live prices so recomputation is visually obvious (ui_design §6). The first
 * composition renders statically; only subsequent changes pulse.
 */
@Composable
fun PulsingText(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }
    var isFirstValue by remember { mutableStateOf(true) }

    LaunchedEffect(text) {
        if (isFirstValue) {
            isFirstValue = false
            return@LaunchedEffect
        }
        scale.snapTo(1f)
        scale.animateTo(1.12f, tween(durationMillis = 90))
        scale.animateTo(1f, tween(durationMillis = 110))
    }

    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
    )
}
