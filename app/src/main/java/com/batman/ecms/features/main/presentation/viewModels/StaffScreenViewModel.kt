package com.batman.ecms.features.main.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.main.data.dto.toStaffData
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.StaffData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StaffScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<StaffData>>>(UiState.Loading)
    val state: StateFlow<UiState<List<StaffData>>> = _state

    fun fetchStaffs(eventId: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val response = RetrofitInstance.apiService.getStaffsByEventId(
                    token = "Bearer ${AuthUserObject.jwt.toString()}",
                    eventId = eventId
                )
                Log.d("darwinkoirala",response.body().toString())
                if (response.isSuccessful) {
                    _state.value = UiState.Success(response.body()!!.map { it.toStaffData() })
                    return@launch
                }
                _state.value = UiState.Error(response.body().toString())
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message.toString())
            }
        }
    }
}