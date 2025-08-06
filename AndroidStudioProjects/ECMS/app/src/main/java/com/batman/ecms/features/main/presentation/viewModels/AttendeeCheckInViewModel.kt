package com.batman.ecms.features.main.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.main.data.dto.toCheckInAttendees
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.CheckedInAttendees
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttendeeCheckInViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<CheckedInAttendees>>>(UiState.Loading)
    val state: StateFlow<UiState<List<CheckedInAttendees>>> = _state

    fun fetchCheckIns(attendeeId: String) {
        viewModelScope.launch {
            Log.d("darwinkoirala", "attendeeId:$attendeeId")
            _state.value = UiState.Loading
            try {

                val response = RetrofitInstance.apiService.getCheckInByAttendee(
                    "Bearer ${AuthUserObject.jwt}", attendeeId
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _state.value = UiState.Success(body.map { it.toCheckInAttendees() })
                    } else {
                        _state.value = UiState.Error("No Checkins")
                    }
                } else {
                    _state.value = UiState.Error("Request failed with code ${response.code()}")
                }


            } catch (e: Exception) {
                _state.value = UiState.Error(message = e.message.toString())
            }

        }


    }

}
