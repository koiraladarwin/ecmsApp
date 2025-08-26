package com.batman.ecms.features.main.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.batman.ecms.R


@Composable
fun NotificationsScreen() {
    val notifications = listOf(
        NotificationItem(
            profileImage = "https://path_to_profile_image_1",
            title = "Ashwin Bose",
            message = "is requesting access to Design File - Final Project.",
            time = "2m"
        ),
        NotificationItem(
            profileImage = "https://path_to_profile_image_2",
            title = "Patrick",
            message = "added a comment on Design Assets - Smart Tags file: \"Looks perfect, send it for technical review tomorrow\"",
            time = "8h"
        ),
        NotificationItem(
            profileImage = "https://path_to_profile_image_3",
            title = "New Feature Alert!",
            message = "We're pleased to introduce the latest enhancements in our templating experience.",
            time = "14h"
        ),
        NotificationItem(
            profileImage = "https://path_to_profile_image_4",
            title = "Samantha",
            message = "has shared a file with you: Demo File.pdf 2.2 MB",
            time = "14h"
        )
    )

    LazyColumn(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        items(notifications) { notification ->
            Box(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            ){
                NotificationCard(notification)
            }
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationItem) {

    Card(elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = "",
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold
                )
                Text(text = notification.message)
                Text(
                    text = notification.time,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

data class NotificationItem(
    val profileImage: String,
    val title: String,
    val message: String,
    val time: String
)

