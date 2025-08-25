package com.batman.ecms.features.main.data.dto

import com.batman.ecms.features.main.domain.models.StaffMemberData

data class StaffDto(
    val firebase_id: String,
    val name: String,
    val email: String,
    val image_url: String,
    val can_see_scanned: Boolean,
    val can_add_attendee: Boolean,
    val can_see_attendee: Boolean
)
fun StaffDto.toStaffData(): StaffMemberData {
    return StaffMemberData(
        firebaseId = firebase_id,
        name = name,
        email = email,
        imageUrl = image_url,
        canSeeScanned = can_see_scanned,
        canAddAttendee = can_add_attendee,
        canSeeAttendee = can_see_attendee
    )
}
