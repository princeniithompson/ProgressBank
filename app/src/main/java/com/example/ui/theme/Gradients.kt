package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Named theme brushes.
 * Selected style: Blended Indigo Gradient.
 */
object AppGradients {
    // Signature Blended Indigo Gradient
    val PrimaryIndigoGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF0F1E2C),
            Color(0xFF1B3B6F),
            Color(0xFF284B77)
        )
    )

    val PrimaryVerticalIndigoGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E3A5F),
            Color(0xFF0F1E2C)
        )
    )

    // Soft hero circle gradient for the large "+" button
    val HeroCircleGradient = Brush.radialGradient(
        colors = listOf(
            Color(0xFFE2EFFF),
            Color(0xFFCEE2F6)
        )
    )

    // Gentle container gradient for highlight cards
    val ContainerHighlightGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFF9F1FD)
        )
    )

    // Dark terminal code block header gradient
    val CodeHeaderGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF1B2A38),
            Color(0xFF253342)
        )
    )
}
