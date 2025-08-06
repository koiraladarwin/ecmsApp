package com.batman.ecms.features.main.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.batman.ecms.features.main.domain.models.CheckedInAttendees

@Composable
fun TicketItem(ticket: CheckedInAttendees) {
    Card(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.Companion.padding(16.dp)) {

            Text(
                text = ticket.activityName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Companion.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.Companion.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Text(
                    text = "${ticket.personName}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${ticket.role}-${ticket.autoId}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "${ticket.scannedAt}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Scanned By: ${ticket.scannedBy}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}