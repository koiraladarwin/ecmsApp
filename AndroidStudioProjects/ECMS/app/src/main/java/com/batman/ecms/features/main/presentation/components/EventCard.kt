package com.batman.ecms.features.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun EventCard(
    imageUrl: String,
    title: String,
    date: String,
    location: String,
    attendees: Int,
    staffs: Int,
    onClickAttendees: () -> Unit,
    onClickStaff: () -> Unit,
    onClickActivity: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Box(modifier = Modifier.height(179.dp)) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Event Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                    Text(
                        location,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }

            Divider()


            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onClickAttendees)
                        .padding(16.dp)
                ) {
                    Text("Attendees", style = MaterialTheme.typography.labelMedium)
                    Text(
                        "$attendees",
                        color = Color(0xFF177F37),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onClickStaff)
                        .padding(16.dp)
                ) {
                    Text("Staffs", style = MaterialTheme.typography.labelMedium)
                    Text(
                        "$staffs",
                        color = Color(0xFF2196F3),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onClickActivity)
                        .padding(23.dp)
                ) {
                    Box() {
                        Icon(Icons.Default.Edit, contentDescription = "Info")
                    }
                }
            }

        }
    }
}
