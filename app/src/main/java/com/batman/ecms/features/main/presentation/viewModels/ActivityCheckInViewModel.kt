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

class ActivityCheckInViewModel: ViewModel() {
    private val _state = MutableStateFlow<UiState<List<CheckedInAttendees>>>(UiState.Loading)
    val state: StateFlow<UiState<List<CheckedInAttendees>>> = _state

    fun fetchCheckIns(activityId:String){
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val data = RetrofitInstance.apiService.getCheckInByActivity("Bearer ${AuthUserObject.jwt.toString()}",activityId)
                _state.value = UiState.Success(data.map { it.toCheckInAttendees() })
                Log.d("darwinkoirala","here")
                Log.d("darwinkoirala",data.toString())
            }catch (e: Exception){
                _state.value = UiState.Error(message = e.message.toString())
                Log.d("darwinkoirala",e.message.toString())
            }

        }


    }

}