package com.batman.ecms.features.main.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.common.constants.MessageConst
import com.batman.ecms.features.main.data.dto.toEventData
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.EventInfoData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

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

}