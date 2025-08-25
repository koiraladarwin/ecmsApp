package com.batman.ecms.features.main.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import com.batman.ecms.UiState
import com.batman.ecms.features.main.data.dto.toUserData
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.domain.models.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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


    private val _roles = MutableStateFlow<List<String>>(emptyList())
    val roles: StateFlow<List<String>> = _roles


    private val _selectedRole = MutableStateFlow("ALL")
    val selectedRole = _selectedRole

    fun selectRole(role: String) {
        _selectedRole.value = role
    }


    init {
        viewModelScope.launch {
            combine(
                _text.debounce(300).distinctUntilChanged(),
                _selectedRole,
                _state
            ) { query, role, state ->
                if (state is UiState.Success) {
                    val filtered = attendeesList.filter { user ->
                        val q = query.trim().lowercase()
                        val matchesSearch = user.name.lowercase().contains(q) ||
                                user.role.lowercase().contains(q) ||
                                user.autoId.toString().contains(q)
                        val matchesRole = (role == "ALL") || user.role == role
                        matchesSearch && matchesRole
                    }
                    UiState.Success(filtered)
                } else {
                    state
                }
            }.collectLatest {
                _state.value = it
            }
        }
    }


    fun fetchUsers(eventId: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            val token = AuthUserObject.getJwt()
            try {
                val response = RetrofitInstance.apiService.getUsersByEvent(
                    eventId = eventId,
                    token = token
                )
                val data = response.map { it.toUserData() }
                attendeesList = data
                _state.value = UiState.Success(data)

                val uniqueRoles = data.map { it.role }.toSet().toList().sorted()
                _roles.value = listOf("ALL") + uniqueRoles
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }


    fun type(text: String) {
        _text.value = text
    }

}