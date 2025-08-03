package com.darwin.ecms.features.main.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darwin.ecms.MainActivity
import com.darwin.ecms.UiState
import com.darwin.ecms.features.main.data.dto.CheckInRequest
import com.darwin.ecms.features.main.data.service.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.sql.Time

class ScanAttendeeViewModel : ViewModel() {

    private val _scannedText = MutableStateFlow("")
    val scannedText: StateFlow<String> = _scannedText

    private val _scannedReq = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val scannedReq: StateFlow<UiState<Unit>> = _scannedReq

    fun resetUiState() {
        _scannedReq.value = UiState.Loading
    }

    fun scanSuccess(activityId: String, attendeeId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.createCheckIn(
                    checkInRequest = CheckInRequest(
                        attendee_id = attendeeId,
                        activity_id = activityId,
                        scanned_by = ""
                    )
                )
                when (response.code()) {
                201 -> _scannedReq.value = UiState.Success(Unit)
                400 -> _scannedReq.value = UiState.Error("Bad Input")
                409 -> _scannedReq.value = UiState.Error("Already Scanned")
                else -> _scannedReq.value = UiState.Error("Unexpected Error: ${response.code()} Contact Darwin")
            }

            } catch (e: Exception) {
                _scannedReq.value = UiState.Error("Network Issue")
            }

        }
    }

}