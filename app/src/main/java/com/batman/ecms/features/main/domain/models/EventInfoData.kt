package com.batman.ecms.features.main.domain.models

data class EventInfoData(
    val eventId:String,
    val eventName:String,
    val location: String,
    val date:String,
    val attendees: Int,
    val staffs: Int,
    val activities: List<ActivityData>,
    val staffCode:String?
)
