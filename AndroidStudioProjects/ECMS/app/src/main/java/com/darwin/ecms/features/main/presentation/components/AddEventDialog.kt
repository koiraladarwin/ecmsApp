package com.darwin.ecms.features.main.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.darwin.ecms.features.main.domain.models.CreateEventData

@Composable
fun AddEventDialog(
    visible: Boolean,
    onSubmit: (CreateEventData) -> Unit,
    onDismiss: () -> Unit
) {
    if (!visible) return

    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Event") },
        confirmButton = {
            TextButton(onClick = {
                onSubmit(CreateEventData(name, location, startTime, endTime))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Event Name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Event Location") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Start Time") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("End Time") },
                    singleLine = true
                )
            }
        }
    )
}
