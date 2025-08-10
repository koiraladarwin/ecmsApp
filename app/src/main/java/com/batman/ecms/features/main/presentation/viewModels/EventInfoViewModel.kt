package com.batman.ecms.features.main.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.main.data.dto.toEventData
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.EventInfoData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EventInfoViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState<EventInfoData>>(UiState.Loading)
    val state = _state

    fun loadEventInfo(eventId: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getEventInfo(
                    eventId = eventId,
                    token = "Bearer ${AuthUserObject.jwt.toString()}"
                )
                val data = response.toEventData()
                _state.value = UiState.Success(data)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }

    }

}