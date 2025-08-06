package com.batman.ecms.features.main.domain.models

data class CreateEventData(
    val name: String,
    val location: String,
    val startTime: String,
    val endTime: String
)
