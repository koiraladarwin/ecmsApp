package com.batman.ecms.features.main.data.dto


data class UserRequest(
    val full_name: String,
    val company: String,
    val position: String,
    val image_url: String,
    val event_id: String,
    val role: String
)

