package com.darwin.ecms.features.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            EventCard(
                imageUrl = "https://images.unsplash.com/photo-1475359524104-d101d02a042b?w=500",
                title = "Yoga Live Music Workshop",
                date = "Fri May 04 2018 at 12:00 am",
                location = "Sunnyville",
                ticketSales = 26,
                checkedIn = 1
            )

            EventCard(
                imageUrl = "https://images.unsplash.com/photo-1531058020387-3be344556be6?w=500",
                title = "Tech Talks & Startup Demos",
                date = "Sat Sep 14 2025 at 2:00 pm",
                location = "Innovation Hub, Silicon City",
                ticketSales = 112,
                checkedIn = 47
            )

        }
    }
}