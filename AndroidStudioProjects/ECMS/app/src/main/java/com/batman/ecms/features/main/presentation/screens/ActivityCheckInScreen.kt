package com.batman.ecms.features.main.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.batman.ecms.features.main.domain.models.ScannedTicket

@Composable
fun ActivityCheckInScreen(activityId:String){
        val dummyList = listOf(
                ScannedTicket(
                        activityName = "Opening Ceremony",
                        scannedAt = "2025-08-05 10:30 AM",
                        personName = "Alice Smith",
                        scannedBy = "John Doe"
                ),
                ScannedTicket(
                        activityName = "Tech Talk",
                        scannedAt = "2025-08-05 11:00 AM",
                        personName = "Bob Johnson",
                        scannedBy = "Jane Roe"
                )
        )
        ScannedTicketList(scannedTickets = dummyList)
}
@Composable
fun ScannedTicketList(
        scannedTickets: List<ScannedTicket>,
        modifier: Modifier = Modifier
) {
        LazyColumn(
                modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
                items(scannedTickets) { ticket ->
                        TicketItem(ticket)
                }
        }
}

@Composable
fun TicketItem(ticket: ScannedTicket) {
        Card(
                modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
                Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                                text = ticket.activityName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                                text = "Person: ${ticket.personName}",
                                style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                                text = "Date: ${ticket.scannedAt}",
                                style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                                text = "Scanned By: ${ticket.scannedBy}",
                                style = MaterialTheme.typography.bodySmall
                        )
                }
        }
}
