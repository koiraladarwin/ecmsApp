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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.batman.ecms.R
import com.batman.ecms.features.main.presentation.viewModels.SettingItem
import com.batman.ecms.features.main.presentation.viewModels.SettingsViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    onSignOut: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    Scaffold(
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    ) {
                        AsyncImage(
                            model = viewModel.personInfo.value.img,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            placeholder = painterResource(id = R.drawable.placeholder),
                            error = painterResource(id = R.drawable.placeholder)

                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = viewModel.personInfo.value.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = viewModel.personInfo.value.email,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }

                }
                items(viewModel.settings) { item ->
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