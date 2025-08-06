package com.batman.ecms.features.main.domain.models

data class ScannedTicket(
    val activityName: String,
    val scannedAt: String,  // You can use LocalDateTime and format it
    val personName: String,
    val scannedBy: String
)
