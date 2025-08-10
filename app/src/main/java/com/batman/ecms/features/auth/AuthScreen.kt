package com.batman.ecms.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = viewModel(),
    onStartSignIn: () -> Unit,
) {
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (authState) {
            is AuthState.Idle -> {
                Button(onClick = onStartSignIn) {
                    Text("Sign in with Google")
                }
            }

            is AuthState.Loading -> {
                CircularProgressIndicator()
            }

            is AuthState.Error -> {
                Text(
                    "Error: ${(authState as AuthState.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = onStartSignIn) {
                    Text("Try Again")
                }
            }

            is AuthState.Success -> {}
        }
    }
}
