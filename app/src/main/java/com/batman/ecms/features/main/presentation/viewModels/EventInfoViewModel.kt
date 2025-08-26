package com.batman.ecms.features.main.presentation.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.common.constants.MessageConst
import com.batman.ecms.features.common.utils.toIsoString
import com.batman.ecms.features.main.data.dto.ActivityRequest
import com.batman.ecms.features.main.data.dto.UserRequest
import com.batman.ecms.features.main.data.dto.toEventData
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.EventInfoData
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

class EventInfoViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState<EventInfoData>>(UiState.Loading)
    val state = _state

    fun loadEventInfo(eventId: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            val token = AuthUserObject.getJwt()
            try {
                val response = RetrofitInstance.apiService.getEventInfo(
                    eventId = eventId,
                    token = token
                )
                when (response.code()) {
                    200 -> _state.value = UiState.Success(response.body()!!.toEventData())
                    500 -> _state.value = UiState.Error(MessageConst.SERVERERROR)
                    else -> _state.value = UiState.Error(MessageConst.UNKNOWN)
                }
            } catch (e: IOException) {
                _state.value = UiState.Error(MessageConst.NOINTERNET)
            } catch (e: Exception) {
                _state.value = UiState.Error(MessageConst.UNKNOWN)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addActivity(
        name: String,
        type: String,
        startTime: String,
        endTime: String,
        eventId: String
    ): String? {
        return try {
            val token = AuthUserObject.getJwt()
            val response = RetrofitInstance.apiService.addActivity(
                token = token, user = ActivityRequest(
                    event_id = eventId,
                    name = name,
                    type = type,
                    start_time = startTime,
                    end_time = endTime
                )
            )
            when (response.code()) {
                201 -> null
                401 -> MessageConst.ACESSDENIED
                else -> MessageConst.SERVERERROR
            }
        } catch (e: IOException) {
            MessageConst.NOINTERNET
        } catch (e: Exception) {
            MessageConst.UNKNOWN
        }
    }


}