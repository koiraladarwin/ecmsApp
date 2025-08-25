package com.batman.ecms.features.main.data.dto

data class EditRoleDto(
    val firebase_id: String,
    val event_id:String,
    val can_add_attendee: Boolean,
    val can_see_attendee: Boolean,
    val can_see_scanned: Boolean,
)