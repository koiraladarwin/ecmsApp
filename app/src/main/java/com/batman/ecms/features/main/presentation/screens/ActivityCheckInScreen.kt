package com.batman.ecms.features.main.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batman.ecms.UiState
import com.batman.ecms.features.common.components.CustomLoader
import com.batman.ecms.features.main.presentation.components.DefaultTopAppBar
import com.batman.ecms.features.main.presentation.components.MainTopAppBar
import com.batman.ecms.features.main.presentation.components.NavBackTopAppBar
import com.batman.ecms.features.main.presentation.components.ScannedTicketList
import com.batman.ecms.features.main.presentation.viewModels.ActivityCheckInViewModel

@Composable
fun ActivityCheckInScreen(
    activityId: String,
    viewModel: ActivityCheckInViewModel = viewModel(),
    navBack: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchCheckIns(activityId)
    }
    Scaffold(
        topBar = {
            NavBackTopAppBar("Checkins", navBack = navBack)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val value = state.value) {
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(value.message)
                    }
                }

                UiState.Loading -> {
                    CustomLoader()
                }

                is UiState.Success -> {
                    ScannedTicketList(scannedTickets = value.data)
                }

            }

        }

    }

}

