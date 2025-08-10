package com.batman.ecms.features.main.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batman.ecms.UiState
import com.batman.ecms.features.main.domain.models.StaffData
import com.batman.ecms.features.main.presentation.components.PersonInfoCard
import com.batman.ecms.features.main.presentation.components.StaffInfo
import com.batman.ecms.features.main.presentation.viewModels.StaffScreenViewModel

@Composable
fun StaffScreen(
    eventId: String = "409f7350-31c0-4a97-ac31-0adceb48f816",
    viewModel: StaffScreenViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchStaffs(eventId)
    }

    val state = viewModel.state.collectAsState()
    when (val value = state.value) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${value.message}", color = Color.Red)
            }
        }

        is UiState.Success<List<StaffData>> -> {
            Column {
                Box(modifier = Modifier.height(10.dp))
                LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
                    items(value.data) {staff->
                        StaffInfo(
                            name = staff.name,
                            email = staff.email,
                            imageUrl = staff.imageUrl,
                            canSeeScanned = staff.canSeeScanned,
                            canSeeAttendee = staff.canSeeAttendee,
                            canAddAttendee = staff.canAddAttendee,
                            onSave = { a, b, c ->

                            },
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                }
            }


        }
    }
//    Column(
//        modifier = Modifier
//            .padding(horizontal = 10.dp)
//            .padding(top = 15.dp)
//    ) {
//        StaffInfo(
//            name = "Bruce Wayne",
//            email = "darwinkoirala123@gmail.com",
//            imageUrl = "https://randomuser.me/api/portraits/men/30.jpg",
//            onSave = { a, b, c ->
//
//            },
//        )
//        Spacer(modifier = Modifier.size(10.dp))
//        PersonInfoCard(
//            name = "Bruce Wayne",
//            code = "MEM-2",
//            position = "Executive",
//            company = "OCS Business Solution",
//            imageUrl = "https://randomuser.me/api/portraits/men/30.jpg",
//            onClickLogs = {},
//        )
//    }
}
