package com.sfaxdroid.wallpapers.core.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape


fun Modifier.shimmer(
    isLoading: Boolean,
    shape: Shape = RectangleShape
): Modifier = composed {

    if (!isLoading) return@composed this

    val transition = rememberInfiniteTransition(label = "shimmer")

    val xShimmer by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            )
        ),
        label = "shimmerAnim"
    )

    val colors = listOf(
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant
    )

    background(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(xShimmer, 0f),
            end = Offset(xShimmer + 400f, 0f)
        ),
        shape = shape
    )
}