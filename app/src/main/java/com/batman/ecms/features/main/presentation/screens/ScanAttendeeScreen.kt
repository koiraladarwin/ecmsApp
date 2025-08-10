package com.batman.ecms.features.main.presentation.screens

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batman.ecms.UiState
import com.batman.ecms.features.main.presentation.components.QrScannerView
import com.batman.ecms.features.main.presentation.viewModels.ScanAttendeeViewModel
import kotlinx.coroutines.delay

@Composable
fun ScanAttendeeScreen(
    activityId: String,
    viewModel: ScanAttendeeViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val scannedReq by viewModel.scannedReq.collectAsState()
    val scannedText by viewModel.scannedText.collectAsState()

    var scanned by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    // Request camera permission on first composition
    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        } else {
            hasPermission = true
        }
    }

    // Show camera only if permission is granted
    if (!hasPermission) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera permission is required to scan QR codes.")
        }
        return
    }

    // React to scan result
    LaunchedEffect(scannedReq) {
        if (scannedReq !is UiState.Loading) {
            showDialog = true
        }
    }

    if (!scanned) {
        QrScannerView { attendeeId ->
            scanned = true
            viewModel.scanSuccess(activityId = activityId, attendeeId = attendeeId)
        }
        return
    }

    // Success/Error dialog after scan
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (showDialog) {
            Dialog(onDismissRequest = {}) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (scannedReq) {
                        is UiState.Loading -> CircularProgressIndicator()
                        is UiState.Success -> SuccessDialogContent()
                        is UiState.Error -> ErrorDialogContent((scannedReq as UiState.Error).message)
                    }
                }
            }

            // Reset scan after short delay
            LaunchedEffect(Unit) {
                delay(2000)
                showDialog = false
                scanned = false
                viewModel.resetUiState()
            }
        }
    }
}

@Composable
fun SuccessDialogContent() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text("Check-in Successful", color = Color.Black)
    }
}

@Composable
fun ErrorDialogContent(error: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Failed",
            tint = Color.Red,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text("Failed: $error", color = Color.Black)
    }
}
