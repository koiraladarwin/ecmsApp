package com.batman.ecms.features.auth

import android.app.Application
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserData) : AuthState()
    data class Error(val message: String) : AuthState()
}


class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AuthViewModel"

    private val client = GoogleAuthUiClient(application.applicationContext)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {
        viewModelScope.launch {
            Log.d(TAG, "Checking if user already signed in...")
            val user = client.getSignedInUser()
            if (user != null) {
                Log.d(TAG, "User found: ${user.userId}")
                _authState.value = AuthState.Success(user)
            } else {
                Log.d(TAG, "No signed-in user.")
                _authState.value = AuthState.Idle // make this explicit
            }
        }
    }

    suspend fun beginSignIn(): IntentSender? {
        _authState.value = AuthState.Loading
        return try {
            Log.d(TAG, "Begin sign-in flow...")
            client.beginSignIn()
        } catch (e: Exception) {
            Log.e(TAG, "Begin sign-in failed: ${e.message}", e)
            _authState.value = AuthState.Error(e.localizedMessage ?: "Failed to start sign-in")
            null
        }
    }

    fun handleSignInIntent(intent: Intent) {

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                Log.d(TAG, "Handling sign-in intent...")
                val user = client.signInWithIntent(intent)
                if (user != null) {
                    Log.d(TAG, "Sign-in successful: ${user.userId}")
                    _authState.value = AuthState.Success(user)
                } else {
                    Log.e(TAG, "Sign-in failed: user is null")
                    _authState.value = AuthState.Error("Sign-in failed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during sign-in: ${e.message}", e)
                _authState.value = AuthState.Error(e.localizedMessage ?: "Sign-in failed")
            }
        }
    }

    fun signOut() {
        Log.d(TAG, "Signing out...")
        client.signOut()
        _authState.value = AuthState.Idle
    }
}
