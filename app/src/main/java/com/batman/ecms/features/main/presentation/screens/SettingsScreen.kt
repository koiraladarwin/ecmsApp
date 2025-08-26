package com.batman.ecms.features.main.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


data class SettingItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    userName: String = "Batman",
    userEmail: String = "xyz123@gmail.com",
    onSignOut: () -> Unit
) {
    val settings = listOf(
        SettingItem(
            Icons.Default.Person,
            "Account",
            "Security notifications, change number"
        ) { /* TODO */ },
        SettingItem(
            Icons.Default.Lock,
            "Privacy",
            "Block contacts, disappearing messages"
        ) { /* TODO */ },
        SettingItem(Icons.Default.Face, "Avatar", "Create, edit, profile photo") { /* TODO */ },
        SettingItem(
            Icons.AutoMirrored.Default.Chat,
            "Chats",
            "Theme, wallpapers, chat history"
        ) { /* TODO */ },
        SettingItem(
            Icons.Default.Notifications,
            "Notifications",
            "Message, group & call tones"
        ) { /* TODO */ },
        SettingItem(
            Icons.Default.Storage,
            "Storage and data",
            "Network usage, auto-download"
        ) { /* TODO */ },
        SettingItem(
            Icons.Default.Language,
            "App language",
            "English (device's language)"
        ) { /* TODO */ },
        SettingItem(
            Icons.AutoMirrored.Default.Help,
            "Help",
            "Help centre, contact us, privacy policy"
        ) { /* TODO */ }
    )

    Scaffold(
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding()
                .padding(horizontal = 16.dp)
        ) {
            // Profile Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                AsyncImage(
                    model = "https://randomuser.me/api/portraits/men/44.jpg",
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = userEmail,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Settings items
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyColumn {
                    items(settings) { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { item.onClick() }
                                .padding(vertical = 16.dp)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = item.subtitle,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Divider(color = Color.LightGray.copy(alpha = 0.3f))
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))

                        // Sign Out button
                        Button(
                            onClick = onSignOut,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ExitToApp,
                                contentDescription = "Sign Out"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sign Out")
                        }
                    }

                }
            }
        }
    }
}