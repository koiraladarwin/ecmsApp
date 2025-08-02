package com.darwin.ecms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darwin.ecms.features.auth.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    user: UserData,
    onSignOut: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ECMS Home") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome, ${user.userName}")
            Spacer(Modifier.height(16.dp))
            Button(onClick = onSignOut) {
                Text("Sign Out")
            }
        }
    }
}
