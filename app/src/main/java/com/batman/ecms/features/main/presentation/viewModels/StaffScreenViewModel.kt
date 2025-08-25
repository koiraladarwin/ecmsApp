package com.batman.ecms.features.main.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.main.data.dto.EditRoleDto
import com.batman.ecms.features.main.data.dto.toStaffData
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.StaffData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException

class StaffScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<StaffData>>>(UiState.Loading)
    val state: StateFlow<UiState<List<StaffData>>> = _state

    fun fetchStaffs(eventId: String) {
        viewModelScope.launch {
            val token = AuthUserObject.getJwt()
            try {
                val response = RetrofitInstance.apiService.getStaffsByEventId(
                    token = token,
                    eventId = eventId
                )
                if (response.isSuccessful) {
                    _state.value = UiState.Success(response.body()!!.map { it.toStaffData() })
                    return@launch
                }
                _state.value = UiState.Error(response.body().toString())
            } catch (_: IOException) {
                _state.value = UiState.Error("Network error")
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message.toString())
            }
        }
    }

    suspend fun modifyStaffRole(
        firebaseId: String,
        canSeeScanned: Boolean,
        canSeeAttendee: Boolean,
        canAddAttendee: Boolean,
        eventId: String,
    ): Boolean {
        val token = AuthUserObject.getJwt()
        return try {
            val res = RetrofitInstance.apiService.modifyStaffRole(
                token = token,
                editRoleDto = EditRoleDto(
                    can_add_attendee = canAddAttendee,
                    can_see_attendee = canSeeAttendee,
                    can_see_scanned = canSeeScanned,
                    firebase_id = firebaseId,
                    event_id = eventId
                )
            )
            return res.isSuccessful
        } catch (e: Exception) {
            return false
        }
    }
}