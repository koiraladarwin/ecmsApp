package com.batman.ecms.features.main.presentation.screens

import android.widget.Toast
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batman.ecms.UiState
import com.batman.ecms.features.common.components.CustomLoader
import com.batman.ecms.features.main.domain.models.StaffMemberData
import com.batman.ecms.features.main.domain.models.StaffScreenData
import com.batman.ecms.features.main.presentation.components.DefaultTopAppBar
import com.batman.ecms.features.main.presentation.components.NavBackTopAppBar
import com.batman.ecms.features.main.presentation.components.StaffInfo
import com.batman.ecms.features.main.presentation.viewModels.StaffScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun StaffScreen(
    eventId: String,
    viewModel: StaffScreenViewModel = viewModel(),
    navBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchStaffs(eventId)
    }

    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = { NavBackTopAppBar("Staff", navBack = navBack) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val value = state.value) {
                is UiState.Loading -> {
                    CustomLoader()
                }

                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${value.message}", color = Color.Red)
                    }
                }

                is UiState.Success<StaffScreenData> -> {

                    Column {
                        Box(modifier = Modifier.height(10.dp))
                        LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
                            items(value.data.data) { staff ->
                                StaffInfo(
                                    name = staff.name,
                                    email = staff.email,
                                    imageUrl = staff.imageUrl,
                                    canSeeScanned = staff.canSeeScanned,
                                    canSeeAttendee = staff.canSeeAttendee,
                                    canAddAttendee = staff.canAddAttendee,
                                    onSave = { seeAtt, addAtt, seeScan ->
                                        coroutineScope.launch {
                                            val ok = viewModel.modifyStaffRole(
                                                firebaseId = staff.firebaseId,
                                                canSeeScanned = seeAtt,
                                                canAddAttendee = addAtt,
                                                canSeeAttendee = seeScan,
                                                eventId = eventId
                                            )
                                            viewModel.fetchStaffs(eventId)
                                            Toast.makeText(
                                                context,
                                                if (ok) "Staff Role Changed" else "Failed To Change",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    },
                                    isModifying = value.data.isModifying
                                )
                                Spacer(modifier = Modifier.size(10.dp))
                            }
                        }
                    }
                }
            }

        }
    }
}
