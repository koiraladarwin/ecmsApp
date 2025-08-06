package com.batman.ecms.features.main.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.batman.ecms.features.main.domain.models.CheckedInAttendees

@Composable
fun ScannedTicketList(
    scannedTickets: List<CheckedInAttendees>,
    modifier: Modifier = Modifier.Companion
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