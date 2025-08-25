package com.batman.ecms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.IntentSenderRequest
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.batman.ecms.features.auth.AuthScreen
import com.batman.ecms.features.auth.AuthState
import com.batman.ecms.features.auth.AuthViewModel
import com.batman.ecms.features.auth.UserData
import com.batman.ecms.ui.theme.EcmsTheme

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val intent = result.data
            if (intent != null) {
                authViewModel.handleSignInIntent(intent)
            }
        } else {

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcmsTheme {
                var stateLoaded by remember { mutableStateOf(false) }
                var signedIn by remember { mutableStateOf<UserData?>(null) }
                val authState by authViewModel.authState.collectAsState()

                LaunchedEffect(authState) {
                    stateLoaded = true
                    signedIn = if (authState is AuthState.Success) {
                        (authState as AuthState.Success).user
                    } else null
                }

                if (!stateLoaded) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                    return@EcmsTheme
                }
                signedIn?.let {
                    MainLayout(onSignOut = {
                        authViewModel.signOut()
                    })
                    return@EcmsTheme
                }

                AuthScreen(
                    authViewModel = authViewModel,
                    onStartSignIn = {
                        lifecycleScope.launch {
                            val intentSender = authViewModel.beginSignIn()
                            if (intentSender != null) {
                                val request = IntentSenderRequest.Builder(intentSender).build()
                                signInLauncher.launch(request)
                            }
                        }
                    }
                )
            }
        }
    }

}
