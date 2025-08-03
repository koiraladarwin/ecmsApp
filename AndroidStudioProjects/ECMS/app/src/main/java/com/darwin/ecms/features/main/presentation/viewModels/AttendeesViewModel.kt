package com.darwin.ecms.features.main.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darwin.ecms.UiState
import com.darwin.ecms.features.main.data.dto.toUserData
import com.darwin.ecms.features.main.data.service.RetrofitInstance
import com.darwin.ecms.features.main.domain.models.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class AttendeesViewModel : ViewModel() {
    private var attendeesList: List<UserData> = emptyList()

    private val _state = MutableStateFlow<UiState<List<UserData>>>(UiState.Loading)
    val state = _state

    private val _text = MutableStateFlow("")
    val text = _text


    init {
        viewModelScope.launch {
            _text
                .debounce(300)
                .distinctUntilChanged()
                .combine(_state) { query, currentState ->
                    if (currentState is UiState.Success) {
                        val filtered =attendeesList.filter { user ->
                            val q = query.trim().lowercase()
                            user.name.lowercase().contains(q) ||
                                    user.role.lowercase().contains(q) ||
                                    user.autoId.toString().contains(q)
                        }
                        UiState.Success(filtered)
                    } else {
                        currentState
                    }
                }
                .collectLatest { filteredState ->
                    _state.value = filteredState
                }
        }
    }


    fun fetchUsers(eventId: String) {
        viewModelScope.launch {
            try {
                _state.value = UiState.Loading
                val response = RetrofitInstance.apiService.getUsersByEvent(eventId)
                val data = response.map { it.toUserData() }
                attendeesList = data
                _state.value = UiState.Success(attendeesList)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown Error")
            }

        }
    }

    fun type(text: String) {
        _text.value = text
    }

}