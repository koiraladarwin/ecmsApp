package com.batman.ecms.features.main.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batman.ecms.UiState
import com.batman.ecms.features.main.presentation.components.ScannedTicketList
import com.batman.ecms.features.main.presentation.viewModels.AttendeeCheckInViewModel

@Composable
fun AttendeeCheckInScreen(
    activityId: String,
    viewModel: AttendeeCheckInViewModel = viewModel()
) {
    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchCheckIns(activityId)
    }
    when (val value = state.value) {
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(value.message)
            }
            return
        }

        UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return

        }

        is UiState.Success -> {
            ScannedTicketList(scannedTickets = value.data)
        }

    }

}

