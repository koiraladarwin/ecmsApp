package com.batman.ecms.features.main.presentation.screens

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batman.ecms.UiState
import com.batman.ecms.features.common.components.CustomLoader
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

    if (!hasPermission) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera permission is required to scan QR codes.")
        }
        return
    }

    LaunchedEffect(scannedReq) {
        if (scannedReq !is UiState.Loading) {

        }
    }


    if (!scanned) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            QrScannerView { attendeeId ->
                scanned = true
                viewModel.scanSuccess(activityId = activityId, attendeeId = attendeeId)
            }

            val boxSize = 250.dp
            val infiniteTransition = rememberInfiniteTransition()
            val alpha by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black.copy(alpha = 0.5f))
                )
                Row(modifier = Modifier.height(boxSize)) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.Black.copy(alpha = 0.5f))
                    )
                    Box(
                        modifier = Modifier
                            .size(boxSize)
                            .clip(RoundedCornerShape(12.dp))
                            .border(2.dp, Color.Black.copy(alpha=0.6f))
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Color.Red.copy(alpha=alpha))
                        )
                    }
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.Black.copy(alpha = 0.5f))
                    )
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black.copy(alpha = 0.5f))
                )
            }
        }

        return
    }


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
                        is UiState.Loading -> CustomLoader()
                        is UiState.Success -> SuccessDialogContent()
                        is UiState.Error -> ErrorDialogContent((scannedReq as UiState.Error).message)
                    }
                }
            }

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
