package com.batman.ecms.features.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.batman.ecms.R

@Composable
fun StaffInfo(
    name: String,
    email: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    canSeeAttendee: Boolean,
    canAddAttendee: Boolean,
    canSeeScanned: Boolean,
    isModifying: Boolean,
    onSave: (Boolean, Boolean, Boolean) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { showDialog = true },
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .width(6.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.width(12.dp))

            AsyncImage(
                model = imageUrl,
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                }

                Text(
                    text = email, style = MaterialTheme.typography.bodySmall
                )
            }


        }
    }

    if (showDialog) {
        StaffDetailDialog(
            name = name,
            email = email,
            onDismiss = { showDialog = false },
            onSave = { canSeeAttendee, canAddAttendee, canSeeScanned ->
                onSave(canSeeAttendee, canAddAttendee, canSeeScanned)
            },
            canSeeAttendee = canSeeAttendee,
            canAddAttendee = canAddAttendee,
            canSeeScanned = canSeeScanned,
            isModifying = isModifying
        )
    }

}

@Composable
fun StaffDetailDialog(
    name: String,
    email: String,
    onDismiss: () -> Unit,
    canSeeAttendee: Boolean,
    canAddAttendee: Boolean,
    canSeeScanned: Boolean,
    isModifying: Boolean,
    onSave: (
        Boolean,
        Boolean,
        Boolean,
    ) -> Unit
) {
    var newName by remember { mutableStateOf(name) }
    var newEmail by remember { mutableStateOf(email) }

    var canSeeScannedState by remember { mutableStateOf(canSeeScanned) }
    var canAddAttendeeState by remember { mutableStateOf(canAddAttendee) }
    var canSeeAttendeeState by remember { mutableStateOf(canSeeAttendee) }

    AlertDialog(onDismissRequest = {
       if(!isModifying){
           onDismiss()
       }
    }, confirmButton = {
        TextButton(enabled = !isModifying, onClick = {
            onSave(canSeeScannedState, canAddAttendeeState, canSeeAttendeeState)
        }) {
            Text("Save")
        }

    }, dismissButton = {
        TextButton(enabled = !isModifying,onClick = onDismiss) {
            Text("Cancel")
        }
    }, title = {
        Text("Edit Person Details")
    }, text = {
        Box(contentAlignment = Alignment.Center) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Name") },
                    enabled = false
                )
                OutlinedTextField(
                    value = newEmail,
                    onValueChange = { newEmail = it },
                    label = { Text("Company") },
                    enabled = false
                )

                // Checkboxes
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = canSeeScannedState,
                        onCheckedChange = { canSeeScannedState = it },
                        enabled = !isModifying
                    )
                    Text("Can See Scanned")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = canAddAttendeeState,
                        onCheckedChange = { canAddAttendeeState = it },
                        enabled = !isModifying
                    )
                    Text("Can Add Attendee")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = canSeeAttendeeState,
                        onCheckedChange = { canSeeAttendeeState = it },
                        enabled = !isModifying
                    )
                    Text("Can See Attendee")
                }

            }
            if (isModifying) {
                Box(modifier = Modifier.zIndex(1f)) {
                    CircularProgressIndicator()
                }
            }
        }
    })
}

