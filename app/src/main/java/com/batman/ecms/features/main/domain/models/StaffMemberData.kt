package com.batman.ecms.features.main.domain.models

data class StaffMemberData(
    val firebaseId: String,
    val name: String,
    val email: String,
    val imageUrl: String,
    val canSeeScanned: Boolean,
    val canAddAttendee: Boolean,
    val canSeeAttendee: Boolean
)

data class StaffScreenData(
    val isModifying: Boolean = false,
    val data: List<StaffMemberData>,
)
