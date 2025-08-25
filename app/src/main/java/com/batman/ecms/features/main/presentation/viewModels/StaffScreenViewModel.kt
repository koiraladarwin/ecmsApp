package com.batman.ecms.features.main.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.main.data.dto.EditRoleDto
import com.batman.ecms.features.main.data.dto.toStaffData
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.StaffMemberData
import com.batman.ecms.features.main.domain.models.StaffScreenData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException

class StaffScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState<StaffScreenData>>(UiState.Loading)
    val state: StateFlow<UiState<StaffScreenData>> = _state

    fun fetchStaffs(eventId: String) {
        viewModelScope.launch {
            val token = AuthUserObject.getJwt()
            try {
                val response = RetrofitInstance.apiService.getStaffsByEventId(
                    token = token,
                    eventId = eventId
                )
                if (response.isSuccessful) {
                    val data = response.body()!!.map { it.toStaffData() }
                    _state.value =
                        UiState.Success(StaffScreenData(data = data, isModifying = false))
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
        val success = _state.value as UiState.Success<StaffScreenData>
        _state.value =
            UiState.Success(success.data.copy(isModifying = true))

        return try {
            val token = AuthUserObject.getJwt()
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
            _state.value =
                UiState.Success(success.data.copy(isModifying = false))
            return res.isSuccessful
        } catch (e: Exception) {
            _state.value =
                UiState.Success(success.data.copy(isModifying = false))
            return false
        }
    }
}