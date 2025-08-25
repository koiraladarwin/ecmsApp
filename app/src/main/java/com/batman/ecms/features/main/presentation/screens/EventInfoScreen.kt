package com.batman.ecms.features.main.presentation.screens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.batman.ecms.UiState
import com.batman.ecms.features.common.components.CustomLoader
import com.batman.ecms.features.main.domain.models.EventInfoData
import com.batman.ecms.features.main.presentation.components.ActivityStats
import com.batman.ecms.features.main.presentation.components.StatRow
import com.batman.ecms.features.main.presentation.viewModels.EventInfoViewModel
import kotlinx.coroutines.launch
import java.time.Instant


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EventInfoScreen(
    eventId: String?,
    viewModel: EventInfoViewModel = viewModel(),
    navigateToScan: (String) -> Unit,
    navigateToActivityCheckIn: (String) -> Unit,
) {

    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showAddDialog by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    LaunchedEffect(eventId) {
        viewModel.loadEventInfo(eventId.toString())
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showAddDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
    ) {
        when (val value = state.value) {
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(value.message)
                }
                return@Scaffold
            }

            UiState.Loading -> {
                CustomLoader()
                return@Scaffold
            }

            is UiState.Success<EventInfoData> -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1531058020387-3be344556be6?w=500",
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Column() {
                            Text(
                                text = value.data.eventName,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = value.data.location,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(16.dp),
                            ) {
                                StatRow(
                                    label = "Total Attendees",
                                    value = value.data.attendees.toString()
                                )
                                StatRow(
                                    label = "Total Activity",
                                    value = value.data.activities.size.toString()
                                )
                                StatRow(
                                    label = "Total Staffs",
                                    value = "1"
                                )
                                value.data.staffCode?.let{
                                    StatRow(
                                        label = "Invite Code",
                                        value = value.data.staffCode
                                    )
                                }

                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))


                        Text("Activities", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        value.data.activities.forEach {
                            ActivityStats(
                                title = it.title,
                                startTime = it.startTime,
                                endTime = it.endTime,
                                totalGuests = it.total,
                                scannedGuests = it.checkedIn,
                                onClick = { navigateToScan(it.activityId) },
                                onScanClicked = { navigateToActivityCheckIn(it.activityId) }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }

    }
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Activity") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Activity Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = type,
                        onValueChange = { type = it },
                        label = { Text("Type") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text("Start Time (ISO 8601)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End Time (ISO 8601)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        try {
                            val result = viewModel.addActivity(
                                name = name,
                                type = type,
                                startTime = Instant.parse(startTime),
                                endTime = Instant.parse(endTime),
                                eventId = eventId.toString()
                            )
                            if (result == null) {
                                Toast.makeText(context, "Activity added successfully", Toast.LENGTH_SHORT).show()
                                viewModel.loadEventInfo(eventId.toString())
                            } else {
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Invalid date format", Toast.LENGTH_SHORT).show()
                        }
                        showAddDialog = false
                        name = ""
                        type = ""
                        startTime = ""
                        endTime = ""
                    }
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

