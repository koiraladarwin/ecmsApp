package com.batman.ecms.features.main.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.common.constants.MessageConst
import com.batman.ecms.features.main.data.dto.toEventData
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.EventData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel : ViewModel() {
    private val _events = MutableStateFlow<UiState<List<EventData>>>(UiState.Loading)
    val events: StateFlow<UiState<List<EventData>>> = _events

    init {
        fetchEvents()
    }

    suspend fun submitAddEvent(code: String): Boolean {
        try {
            val token = AuthUserObject.getJwt()
            val res = RetrofitInstance.apiService.addEventWithCode(
                token = token,
                code = code
            )
            if (res.isSuccessful) fetchEvents()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            try {
                val token = AuthUserObject.getJwt()
                val response =
                    RetrofitInstance.apiService.getEvents(token = token)
                when (response.code()) {
                    200 -> _events.value = UiState.Success(response.body().orEmpty().map { it.toEventData() })
                    500-> _events.value = UiState.Error(MessageConst.SERVERERROR)
                    else -> _events.value = UiState.Error(MessageConst.UNKNOWN)
                }
            } catch (e: IOException) {
                _events.value = UiState.Error(MessageConst.NOINTERNET)
            } catch (e: Exception) {
                _events.value = UiState.Error(MessageConst.UNKNOWN)
            }
        }
    }
}
