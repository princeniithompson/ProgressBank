package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.viewmodel.AppTab

@Composable
fun TopTabSegment(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Tab 1: Ask
            TabItem(
                title = stringResource(R.string.tab_ask),
                isSelected = selectedTab == AppTab.ASK,
                onClick = { onTabSelected(AppTab.ASK) },
                testTag = "ask_tab_button",
                modifier = Modifier.weight(1f)
            )

            // Tab 2: New Progress
            TabItem(
                title = stringResource(R.string.tab_new_progress),
                isSelected = selectedTab == AppTab.NEW_PROGRESS,
                onClick = { onTabSelected(AppTab.NEW_PROGRESS) },
                testTag = "new_progress_tab_button",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val targetBgColor = if (isSelected) {
        MaterialTheme.colorScheme.surfaceContainerLowest
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }
    val animatedBgColor by animateColorAsState(
        targetValue = targetBgColor,
        animationSpec = tween(durationMillis = 200),
        label = "tabBgAnimation"
    )

    val targetTextColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val shadowModifier = if (isSelected) {
        Modifier.shadow(elevation = 2.dp, shape = RoundedCornerShape(10.dp))
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .then(shadowModifier)
            .background(
                color = animatedBgColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = if (isSelected) {
                MaterialTheme.typography.titleSmall
            } else {
                MaterialTheme.typography.bodyMedium
            },
            color = targetTextColor
        )
    }
}
