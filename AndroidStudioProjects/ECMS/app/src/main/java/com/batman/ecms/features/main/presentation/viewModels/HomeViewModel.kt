package com.batman.ecms.features.main.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.main.data.dto.toEventData
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.EventData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _events = MutableStateFlow<UiState<List<EventData>>>(UiState.Loading)
    val events: StateFlow<UiState<List<EventData>>> = _events

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getEvents(token = "Bearer ${AuthUserObject.jwt.toString()}")
                val mapped = response.map { it.toEventData() }
                _events.value = UiState.Success(mapped)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("darwinkoirala",e.message.toString())
                _events.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
