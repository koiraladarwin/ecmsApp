package com.batman.ecms.features.main.domain.models

data class CheckedInAttendees(
    val activityName: String,
    val scannedAt: String,
    val personName: String,
    val scannedBy: String,
    val userId:String,
    val autoId:String,
    val role:String,

)
