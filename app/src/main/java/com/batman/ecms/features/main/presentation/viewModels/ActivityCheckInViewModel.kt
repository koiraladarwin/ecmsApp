package com.batman.ecms.features.main.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.common.constants.MessageConst
import com.batman.ecms.features.main.data.dto.toCheckInAttendees
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.CheckedInAttendees
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class ActivityCheckInViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<CheckedInAttendees>>>(UiState.Loading)
    val state: StateFlow<UiState<List<CheckedInAttendees>>> = _state

    fun fetchCheckIns(activityId: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val token = AuthUserObject.getJwt()
                val response = RetrofitInstance.apiService.getCheckInByActivity(
                    token = token,
                    activityId = activityId
                )
                when(response.code()){
                    200->{
                        _state.value = UiState.Success(response.body().orEmpty().map { it.toCheckInAttendees() })
                    }
                    500->{
                        _state.value = UiState.Error(MessageConst.SERVERERROR)
                    }
                }
            } catch (e: IOException) {
                _state.value = UiState.Error(MessageConst.NOINTERNET)
            } catch (e: Exception) {
                _state.value = UiState.Error(MessageConst.UNKNOWN)
            }

        }
    }

}