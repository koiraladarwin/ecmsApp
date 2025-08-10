package com.batman.ecms.features.main.data.dto

import com.batman.ecms.features.main.domain.models.UserData


data class UserDtoItem(
    val attendee_id: String,
    val auto_id: Int,
    val company: String,
    val full_name: String,
    val id: String,
    val image_url: String,
    val position: String,
    val role: String
)

fun UserDtoItem.toUserData():UserData{
    return UserData(
        name = full_name,
        role = role,
        autoId = auto_id,
        position = position,
        company = company,
        imgUrl = image_url,
        id = id
    )
}