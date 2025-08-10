package com.batman.ecms.features.main.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.main.data.dto.CheckInRequest
import com.batman.ecms.features.main.data.service.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                Log.d("batmanboxer",attendeeId)
                Log.d("batmanboxer",activityId)
                val response = RetrofitInstance.apiService.createCheckIn(
                    checkInRequest = CheckInRequest(
                        attendee_id = attendeeId,
                        activity_id = activityId,
                    ),
                    token = "Bearer ${AuthUserObject.jwt.toString()}"
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