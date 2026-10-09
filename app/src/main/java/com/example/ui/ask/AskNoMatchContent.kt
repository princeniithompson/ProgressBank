package com.example.ui.ask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.Blueprint
import com.example.ui.components.CircularAddButton
import com.example.ui.components.FlatBlueprintRow
import com.example.ui.theme.AppGradients

@Composable
fun AskNoMatchContent(
    queryText: String,
    existingBlueprints: List<Blueprint>,
    onAddProgressClick: () -> Unit,
    onBlueprintClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User query message (if submitted)
        if (queryText.isNotBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(
                            brush = AppGradients.PrimaryIndigoGradient,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .testTag("no_match_user_query_bubble")
                ) {
                    Text(
                        text = queryText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        // Upper-middle hero: Large circular "+", Label, and Raw message
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularAddButton(
                onClick = onAddProgressClick,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = stringResource(R.string.add_to_progress),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.nothing_similar),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        // Section header: Existing blueprints
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.existing_blueprints).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "${existingBlueprints.size} indexed",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        // Flat title-only list (free rows inside a rounded card)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .testTag("flat_blueprint_list")
        ) {
            Column {
                existingBlueprints.take(4).forEachIndexed { index, bp ->
                    FlatBlueprintRow(
                        title = bp.title,
                        domain = bp.domain,
                        onClick = { onBlueprintClick(bp.id) }
                    )
                    if (index < existingBlueprints.take(4).size - 1) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}
