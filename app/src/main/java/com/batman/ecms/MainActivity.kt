package com.batman.ecms

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.IntentSenderRequest.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.batman.ecms.features.auth.AuthScreen
import com.batman.ecms.features.auth.AuthState
import com.batman.ecms.features.auth.AuthViewModel
import com.batman.ecms.features.auth.UserData
import com.batman.ecms.features.common.components.CustomLoader
import com.batman.ecms.ui.theme.EcmsTheme

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
        RESULT_OK -> {
            result.data?.let {
                authViewModel.handleSignInIntent(it)
            }
        }
        RESULT_CANCELED -> {
            authViewModel.signOut()
        }
        else -> {
            authViewModel.signOut()
        }
    }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcmsTheme {
                val authState by authViewModel.authState.collectAsState()

                when (authState) {
                    is AuthState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomLoader()
                        }
                    }

                    is AuthState.Success -> {
                        MainLayout(onSignOut = { authViewModel.signOut() })
                    }

                    is AuthState.Error -> {
                        AuthScreen(
                            authViewModel = authViewModel,
                            onStartSignIn = {
                                lifecycleScope.launch {
                                    val intentSender = authViewModel.beginSignIn()
                                    intentSender?.let {
                                        val request = Builder(it).build()
                                        signInLauncher.launch(request)
                                    }
                                }
                            }
                        )
                    }

                    is AuthState.Idle -> {
                        AuthScreen(
                            authViewModel = authViewModel,
                            onStartSignIn = {
                                lifecycleScope.launch {
                                    val intentSender = authViewModel.beginSignIn()
                                    intentSender?.let {
                                        val request = Builder(it).build()
                                        signInLauncher.launch(request)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
