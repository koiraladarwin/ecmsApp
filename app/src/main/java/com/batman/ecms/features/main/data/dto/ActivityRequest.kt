package com.batman.ecms.features.main.data.dto
import java.time.Instant

data class ActivityRequest(
    val event_id: String,
    val name: String,
    val type: String,
    val start_time: String,
    val end_time: String,
)


