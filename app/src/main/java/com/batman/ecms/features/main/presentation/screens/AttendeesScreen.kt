package com.batman.ecms.features.main.presentation.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batman.ecms.UiState
import com.batman.ecms.features.main.domain.models.UserData
import com.batman.ecms.features.main.presentation.components.PersonInfoCard
import com.batman.ecms.features.main.presentation.viewModels.AttendeesViewModel
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AttendeesScreen(
    eventId: String,
    onClickCheckInLogs:(String)->Unit,
    viewModel: AttendeesViewModel = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }

    val searchText by viewModel.text.collectAsState()
    val state by viewModel.state.collectAsState()

    val roles by viewModel.roles.collectAsState()
    val selectedRole by viewModel.selectedRole.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.fetchUsers(eventId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Add FAB action here
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Attendees")
            }
        }
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = viewModel::type,
                    placeholder = { Text("Search...") },
                    modifier = Modifier.weight(7.5f)
                )


                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(5.dp)
                        .clickable { expanded = true }
                        .weight(2.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = selectedRole, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown arrow")
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        roles.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role) },
                                onClick = {
                                    viewModel.selectRole(role)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(10.dp))

            when (state) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    Text("Error: ${(state as UiState.Error).message}")
                }

                is UiState.Success -> {
                    val list = (state as UiState.Success<List<UserData>>).data
                    LazyColumn {
                        items(list) { user ->
                            PersonInfoCard(
                                name = user.name,
                                code = "${user.role}-${user.autoId}",
                                position = user.position,
                                company = user.company,
                                imageUrl = user.imgUrl,
                                onClickLogs = {
                                        onClickCheckInLogs(user.id)
                                },
                                onShareClick = {
                                    try {
                                        // 1. Generate QR Code
                                        val barcodeEncoder = BarcodeEncoder()
                                        val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                                            user.id,
                                            BarcodeFormat.QR_CODE,
                                            512,
                                            512
                                        )

                                        // 2. Save to cache directory
                                        val cachePath = File(context.cacheDir, "images")
                                        cachePath.mkdirs()
                                        val file = File(cachePath, "qr_${user.id}.png")
                                        val fileOutputStream = FileOutputStream(file)
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                                        fileOutputStream.close()

                                        // 3. Get content URI
                                        val uri: Uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            file
                                        )

                                        // 4. Share to All Photo Accepting App


                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "image/png"
                                            putExtra(Intent.EXTRA_STREAM, uri)
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }

                                        context.startActivity(Intent.createChooser(shareIntent, "Share image via"))



                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }


            }
        }
    }
}
