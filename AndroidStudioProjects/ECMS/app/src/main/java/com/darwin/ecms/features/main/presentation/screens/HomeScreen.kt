package com.darwin.ecms.features.main.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darwin.ecms.UiState
import com.darwin.ecms.features.main.domain.models.EventData
import com.darwin.ecms.features.main.presentation.components.EventCard
import com.darwin.ecms.features.main.presentation.viewModels.HomeViewModel

@Composable
fun HomeScreen(
    navigateToEventInfo: (String) -> Unit,
    navigateToStaffInfo: (String) -> Unit,
    navigateToAttendeeInfo: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState = viewModel.events.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState.value) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
                return
            }

            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "Error: ${state.message}", color = Color.Red)
                }
                return
            }

            is UiState.Success -> {
                LazyColumn {
                    items(state.data) { event ->
                        EventCard(
                            imageUrl = event.imageUrl,
                            title = event.name,
                            date = event.date,
                            location = event.location,
                            attendees = event.attendees,
                            staffs = event.staffs,
                            onClickActivity = { navigateToEventInfo(event.id) },
                            onClickAttendees = { navigateToAttendeeInfo(event.id) },
                            onClickStaff = { navigateToStaffInfo(event.id) }
                        )
                    }
                }
            }
        }

    }
}
