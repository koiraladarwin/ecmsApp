package com.batman.ecms.features.main.data.dto

import com.batman.ecms.features.main.domain.models.EventData

data class EventsDataDtoItem(
    val description: String,
    val end_time: String,
    val id: String,
    val location: String,
    val name: String,
    val number_of_participant: Int,
    val start_time: String
)

fun EventsDataDtoItem.toEventData(): EventData{
   return EventData(
        id = id,
        name = name,
        imageUrl = "https://images.unsplash.com/photo-1531058020387-3be344556be6?w=500",
        location = location,
        date = start_time,
        attendees = number_of_participant,
        staffs = 0
    )
}