package com.batman.ecms.features.main.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.batman.ecms.features.main.presentation.components.PersonInfoCard

@Composable
fun StaffScreen() {
    Column(modifier = Modifier.padding(horizontal = 10.dp).padding(top = 15.dp)){
        PersonInfoCard(
            name = "Giulio Ciccone",
            code = "MEM-1",
            position = "Executive",
            company = "OCS Business Solution",
            imageUrl = "https://randomuser.me/api/portraits/men/32.jpg",
            onClickLogs = {},
        )
        Spacer(modifier = Modifier.size(10.dp))
        PersonInfoCard(
            name = "Bruce Wayne",
            code = "MEM-2",
            position = "Executive",
            company = "OCS Business Solution",
            imageUrl = "https://randomuser.me/api/portraits/men/30.jpg",
            onClickLogs = {},
        )
    }
}