package com.batman.ecms.features.main.presentation.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batman.ecms.AuthUserObject
import com.batman.ecms.Screen
import com.batman.ecms.UiState
import com.batman.ecms.features.common.components.CustomLoader
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.presentation.components.AddEventDialog
import com.batman.ecms.features.main.presentation.components.EventCard
import com.batman.ecms.features.main.presentation.components.MainTopAppBar
import com.batman.ecms.features.main.presentation.viewModels.HomeViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToEventInfo: (String) -> Unit,
    navigateToStaffInfo: (String) -> Unit,
    navigateToAttendeeInfo: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState = viewModel.events.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    AddEventDialog(
        visible = showDialog,
        onDismiss = { showDialog = false },
        onSubmit = { code ->
            showDialog = false
            coroutineScope.launch {
                val ok = viewModel.submitAddEvent(code)
                if (!ok) Toast.makeText(context,"Failed to add Event", Toast.LENGTH_LONG).show()
            }
        }
    )
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
    },) {
        Box(modifier = Modifier.fillMaxSize()){
            when (val state = uiState.value) {
                is UiState.Loading -> {
                    CustomLoader()
                    return@Scaffold
                }

                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(text = "Error: ${state.message}", color = Color.Red)
                    }
                    return@Scaffold
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
}
