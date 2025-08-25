package com.batman.ecms.features.main.presentation.screens

import android.annotation.SuppressLint
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.batman.ecms.UiState
import com.batman.ecms.features.main.domain.models.EventInfoData
import com.batman.ecms.features.main.presentation.components.ActivityStats
import com.batman.ecms.features.main.presentation.components.StatRow
import com.batman.ecms.features.main.presentation.viewModels.EventInfoViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EventInfoScreen(
    eventId: String?,
    viewModel: EventInfoViewModel = viewModel(),
    navigateToScan: (String) -> Unit,
    navigateToActivityCheckIn: (String) -> Unit,
) {
    LaunchedEffect(eventId) {
        viewModel.loadEventInfo(eventId.toString())
    }
    val state = viewModel.state.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {

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
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
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
}

