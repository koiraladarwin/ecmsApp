package com.darwin.ecms.features.main.data.dto


data class CheckInRequest(
    val attendee_id: String,
    val activity_id: String,
    val scanned_at: String = "2025-07-08T15:30:00Z",
    val status: String = "checked",
    val scanned_by: String
)