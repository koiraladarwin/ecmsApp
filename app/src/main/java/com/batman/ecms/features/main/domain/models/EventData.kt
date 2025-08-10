package com.batman.ecms.features.main.domain.models

data class EventData(
    val id: String,
    val name: String,
    val imageUrl: String,
    val location: String,
    val date: String,
    val attendees: Int,
    val staffs: Int,
)
