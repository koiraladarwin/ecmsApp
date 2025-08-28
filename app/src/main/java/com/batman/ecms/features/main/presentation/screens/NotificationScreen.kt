package com.batman.ecms.features.main.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.batman.ecms.R
import com.batman.ecms.features.main.presentation.components.SearchWithFilterTopAppBar
import com.batman.ecms.features.main.presentation.components.SwipeToDeleteContainer

@Composable
fun NotificationsScreen() {
    val notifications = remember {
        mutableStateListOf(
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
    }
    Scaffold(
        topBar = {
            SearchWithFilterTopAppBar(
                "Alerts",
                dropdownItems = listOf("hours", "days", "months"),
                onQueryChange = {

                },
                onDropdownItemSelected = {

                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            items(
                items = notifications,
                key = { it.profileImage + it.time + it.title }
            ) { notification ->
                SwipeToDeleteContainer(
                    item = notification,
                    onDelete = { item ->
                        notifications.remove(item)
                    },
                ) { currentItem ->
                    NotificationCard(currentItem)
                }
            }
        }

    }
}

@Composable
fun NotificationCard(notification: NotificationItem) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.CenterStart)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = notification.profileImage,
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
                    Text(notification.title, fontWeight = FontWeight.Bold)
                    Text(notification.message)
                    Text(
                        text = notification.time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
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

