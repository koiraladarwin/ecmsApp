package com.batman.ecms.features.common.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.commandiron.compose_loading.ChasingDots

@Composable
fun CustomLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
        , contentAlignment = Alignment.Center
    ) {
        ChasingDots()
    }
}
