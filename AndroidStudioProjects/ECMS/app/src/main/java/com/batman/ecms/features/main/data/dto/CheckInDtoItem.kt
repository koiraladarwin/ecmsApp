package com.batman.ecms.features.main.data.dto

import com.batman.ecms.features.main.domain.models.CheckedInAttendees

data class CheckInDtoItem(
    val activity_id: String,
    val activity_name: String,
    val auto_id: String,
    val full_name: String,
    val id: String,
    val role: String,
    val scanned_at: String,
    val scanned_by: String,
    val status: String,
    val user_id: String
)
fun CheckInDtoItem.toCheckInAttendees(): CheckedInAttendees{
        return CheckedInAttendees(

            activityName = activity_name,
            scannedAt = scanned_at,
            personName = full_name,
            scannedBy = scanned_by,
            userId = user_id,
            autoId = auto_id,
            role = role,
        )
}