package com.batman.ecms.features.main.data.dto

import com.batman.ecms.features.main.domain.models.ActivityData


data class ActivityDto(
    val end_time: String,
    val event_id: String,
    val id: String,
    val name: String,
    val number_of_scaned_users: Int,
    val start_time: String,
    val type: String
)
fun ActivityDto.toActivity(total:Int): ActivityData{
    return ActivityData(
        activityId = id,
        title = name,
        startTime = start_time,
        endTime = end_time,
        checkedIn = number_of_scaned_users,
        total = total
    )
}