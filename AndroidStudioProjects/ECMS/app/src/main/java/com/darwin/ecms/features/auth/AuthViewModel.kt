package com.darwin.ecms.features.auth

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserData) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val client = GoogleAuthUiClient(application.applicationContext)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {
        viewModelScope.launch {
            val user = client.getSignedInUser()
            if (user != null) {
                _authState.value = AuthState.Success(user)
            }
        }
    }

    suspend fun beginSignIn(): android.content.IntentSender? {
        _authState.value = AuthState.Loading
        return try {
            client.beginSignIn()
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage ?: "Failed to start sign-in")
            null
        }
    }

    fun signOut() {
        client.signOut()
        _authState.value = AuthState.Idle
    }

    fun handleSignInIntent(intent: Intent) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = client.signInWithIntent(intent)
                if (user != null) {
                    _authState.value = AuthState.Success(user)
                } else {
                    _authState.value = AuthState.Error("Sign-in failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Sign-in failed")
            }
        }
    }
}
