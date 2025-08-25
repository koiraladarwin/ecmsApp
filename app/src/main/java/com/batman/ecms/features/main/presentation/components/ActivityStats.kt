package com.batman.ecms.features.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActivityStats(
    title: String,
    startTime: String,
    endTime: String,
    totalGuests: Int,
    scannedGuests: Int,
    onClick: () -> Unit,
    onScanClicked: () -> Unit,
) {
    val progress = if (totalGuests > 0) scannedGuests / totalGuests.toFloat() else 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$startTime · $endTime",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color(0xFFE0E0E0)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Scanned $scannedGuests / $totalGuests Guests",
            fontSize = 12.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(
                label = "Total",
                value = totalGuests.toString(),
                modifier = Modifier.weight(1f),
                onClick = {}
            )
            StatCard(
                label = "Scanned",
                value = scannedGuests.toString(),
                modifier = Modifier.weight(1f),
                onClick = onScanClicked
            )
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
