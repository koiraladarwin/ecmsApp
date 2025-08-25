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
import androidx.compose.runtime.rememberUpdatedState
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
            result.data?.let {
                authViewModel.handleSignInIntent(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcmsTheme {
                var stateLoaded by remember { mutableStateOf(false) }
                var signedIn by remember { mutableStateOf<UserData?>(null) }

                val authState by authViewModel.authState.collectAsState()
                val currentAuthState by rememberUpdatedState(authState)

                LaunchedEffect(currentAuthState) {
                    signedIn = if (currentAuthState is AuthState.Success) {
                        (currentAuthState as AuthState.Success).user
                    } else null
                    stateLoaded = true
                }

                when {
                    !stateLoaded -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                        ) {
                        }
                    }

                    signedIn != null -> {
                        MainLayout(onSignOut = { authViewModel.signOut() })
                    }

                    else -> {
                        AuthScreen(
                            authViewModel = authViewModel,
                            onStartSignIn = {
                                lifecycleScope.launch {
                                    val intentSender = authViewModel.beginSignIn()
                                    intentSender?.let {
                                        val request = IntentSenderRequest.Builder(it).build()
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
