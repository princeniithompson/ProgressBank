package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.AppGradients

/**
 * Large circular "+" button for empty/no-match states.
 * Uses Canvas.drawCircle per the house layout rules.
 */
@Composable
fun CircularAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconTint = MaterialTheme.colorScheme.onSecondaryContainer

    Box(
        modifier = modifier
            .size(80.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 40.dp),
                onClick = onClick
            )
            .testTag("add_to_progress_circle_button"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(brush = AppGradients.HeroCircleGradient)
        }

        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_to_progress),
            tint = iconTint,
            modifier = Modifier.size(38.dp)
        )
    }
}
