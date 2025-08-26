package com.batman.ecms.features.main.presentation.screens

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
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
import com.batman.ecms.R
import com.batman.ecms.UiState
import com.batman.ecms.features.common.components.CustomLoader
import com.batman.ecms.features.common.constants.MessageConst
import com.batman.ecms.features.common.utils.formatDate
import com.batman.ecms.features.main.domain.models.EventInfoData
import com.batman.ecms.features.main.presentation.components.ActivityStats
import com.batman.ecms.features.main.presentation.components.StatRow
import com.batman.ecms.features.main.presentation.viewModels.EventInfoViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
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


    val startDateCalenderState = rememberSheetState()
    val lastDateCalenderState = rememberSheetState()

    val currentYear = LocalDate.now().year
    val today = LocalDate.now()
    val pastDates = (1..3650).map { today.minusDays(it.toLong()) }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(eventId) {
        viewModel.loadEventInfo(eventId.toString())
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showAddDialog = true
                //calenderState.show()
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
                                value.data.staffCode?.let {
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
                    DateInputField(
                        label = "Start Time",
                        value = startTime,
                        onClick = { startDateCalenderState.show() }
                    )
                    DateInputField(
                        label = "End Time",
                        value = endTime,
                        onClick = { lastDateCalenderState.show() }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (name.isBlank() || type.isBlank() || startTime.isBlank() || endTime.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    coroutineScope.launch {
                        try {
                            val result = viewModel.addActivity(
                                name = name,
                                type = type,
                                startTime = startTime,
                                endTime = endTime,
                                eventId = eventId.toString()
                            )
                            if (result == null) {
                                Toast.makeText(
                                    context,
                                    "Activity added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewModel.loadEventInfo(eventId.toString())
                            } else {
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, MessageConst.UNKNOWN, Toast.LENGTH_SHORT)
                                .show()
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


    CalendarDialog(
        state = startDateCalenderState,
        selection = CalendarSelection.Date { date ->
            selectedStartDate = date
            showStartTimePicker = true
        },
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            disabledDates = pastDates,
            style = CalendarStyle.MONTH,
            minYear = 1900,
            maxYear = currentYear + 10
        )
    )

    CalendarDialog(
        state = lastDateCalenderState,
        selection = CalendarSelection.Date { date ->
            selectedEndDate = date
            showEndTimePicker = true
        },
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            disabledDates = pastDates,
            style = CalendarStyle.MONTH,
            minYear = 1900,
            maxYear = currentYear + 10
        )
    )

    if (showStartTimePicker && selectedStartDate != null) {
        val timeState = rememberTimePickerState(
            initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
            is24Hour = false
        )

        AlertDialog(
            onDismissRequest = { showStartTimePicker = false },
            title = { Text("Select Start Time") },
            text = {
                androidx.compose.material3.TimePicker(
                    state = timeState
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedStartDate?.let { date ->
                        val iso = date.atTime(timeState.hour, timeState.minute)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toString()
                        startTime = iso
                    }
                    showStartTimePicker = false
                    selectedStartDate = null
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showStartTimePicker = false
                    selectedStartDate = null
                }) { Text("Cancel") }
            }
        )
    }

// Compose-native TimePicker for End Time
    if (showEndTimePicker && selectedEndDate != null) {
        val timeState = rememberTimePickerState(
            initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
            is24Hour = false
        )

        AlertDialog(
            onDismissRequest = { showEndTimePicker = false },
            title = { Text("Select End Time") },
            text = {
                androidx.compose.material3.TimePicker(
                    state = timeState
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedEndDate?.let { date ->
                        val iso = date.atTime(timeState.hour, timeState.minute)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toString()
                        endTime = iso
                    }
                    showEndTimePicker = false
                    selectedEndDate = null
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEndTimePicker = false
                    selectedEndDate = null
                }) { Text("Cancel") }
            }
        )
    }

}

@Composable
fun DateInputField(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (value.isEmpty()) label else formatDate(value),
                style = if (value.isEmpty()) {
                    MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                } else {
                    MaterialTheme.typography.bodyMedium
                }
            )
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Pick Date",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
