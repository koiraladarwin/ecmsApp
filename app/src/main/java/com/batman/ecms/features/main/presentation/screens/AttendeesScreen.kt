package com.batman.ecms.features.main.presentation.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batman.ecms.UiState
import com.batman.ecms.features.common.components.CustomLoader
import com.batman.ecms.features.main.domain.models.UserData
import com.batman.ecms.features.main.presentation.components.DefaultTopAppBar
import com.batman.ecms.features.main.presentation.components.NavBackTopAppBar
import com.batman.ecms.features.main.presentation.components.PersonInfoCard
import com.batman.ecms.features.main.presentation.components.SearchWithFilterTopAppBar
import com.batman.ecms.features.main.presentation.viewModels.AttendeesViewModel
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun AttendeesScreen(
    eventId: String,
    onClickCheckInLogs: (String) -> Unit,
    viewModel: AttendeesViewModel = viewModel(),
    navBack: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val searchText by viewModel.text.collectAsState()
    val state by viewModel.state.collectAsState()

    val roles by viewModel.roles.collectAsState()
    val selectedRole by viewModel.selectedRole.collectAsState()
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    var showAddDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var addingUser by remember { mutableStateOf(false) }

    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchUsers(eventId)
    }

    Scaffold(
        topBar = {
            SearchWithFilterTopAppBar(
                "Attendees",
                dropdownItems = roles,
                onQueryChange = viewModel::enterKeys,
                onDropdownItemSelected = viewModel::selectRole
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showAddDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Attendees")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is UiState.Loading -> {
                    CustomLoader()
                }

                is UiState.Error -> {
                    Text("Error: ${(state as UiState.Error).message}")
                }

                is UiState.Success -> {
                    val list = (state as UiState.Success<List<UserData>>).data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        item{
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        items(list) { user ->
                            PersonInfoCard(
                                name = user.name,
                                code = "${user.role}-${user.autoId}",
                                position = user.position,
                                company = user.company,
                                imageUrl = user.imgUrl,
                                onClick = {
                                    try {
                                        // 1. Generate QR Code
                                        val barcodeEncoder = BarcodeEncoder()
                                        val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                                            user.id,
                                            BarcodeFormat.QR_CODE,
                                            512,
                                            512
                                        )
                                        qrBitmap = bitmap

                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                },
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
                                        bitmap.compress(
                                            Bitmap.CompressFormat.PNG,
                                            100,
                                            fileOutputStream
                                        )
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

                                        context.startActivity(
                                            Intent.createChooser(
                                                shareIntent,
                                                "Share image via"
                                            )
                                        )

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

        qrBitmap?.let {
            Dialog(onDismissRequest = {
                qrBitmap = null
            }) {
                Image(
                    bitmap = qrBitmap!!.asImageBitmap(),
                    contentDescription = "Qr"
                )
            }
        }
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add Attendee") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !addingUser
                        )
                        OutlinedTextField(
                            value = company,
                            onValueChange = { company = it },
                            label = { Text("Company") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !addingUser
                        )
                        OutlinedTextField(
                            value = position,
                            onValueChange = { position = it },
                            label = { Text("Position") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !addingUser
                        )

                        OutlinedTextField(
                            value = role,
                            onValueChange = { role = it },
                            label = { Text("Role") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !addingUser
                        )
                    }
                },
                confirmButton = {
                    TextButton(enabled = !addingUser, onClick = {
                        coroutineScope.launch {
                            addingUser = true
                            val result = viewModel.addAttendee(
                                name = name,
                                company = company,
                                position = position,
                                role = role,
                                eventId = eventId
                            )
                            if (result == null) {
                                Toast.makeText(
                                    context,
                                    "Attendee added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewModel.fetchUsers(eventId)
                            } else {
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                            }

                            showAddDialog = false
                            name = ""
                            company = ""
                            position = ""
                            role = ""
                            addingUser = false
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
}
