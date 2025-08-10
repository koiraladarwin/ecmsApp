package com.batman.ecms.features.main.data.dto

import com.batman.ecms.features.main.domain.models.EventInfoData

data class EventInfoDto(
    val activities: List<ActivityDto>,
    val event: EventDto
)

fun EventInfoDto.toEventData(): EventInfoData {
    return EventInfoData(
        eventId = event.id,
        eventName = event.name,
        location = event.location,
        date = event.start_time,
        attendees = event.number_of_participant,
        staffs = 0,
        activities = activities.map { it.toActivity(event.number_of_participant) },
        staffCode = if (!event.staff_code.isNullOrEmpty()) event.staff_code else null
    )
}