package com.batman.ecms.features.main.domain.models

data class StaffData(
    val firebaseId: String,
    val name: String,
    val email: String,
    val imageUrl: String,
    val canSeeScanned: Boolean,
    val canAddAttendee: Boolean,
    val canSeeAttendee: Boolean
)
