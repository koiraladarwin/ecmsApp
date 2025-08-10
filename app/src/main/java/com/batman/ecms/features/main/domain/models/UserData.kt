package com.batman.ecms.features.main.domain.models

data class UserData(
    val id :String,
    val name: String,
    val role: String,
    val autoId: Int,
    val position: String,
    val company: String,
    val imgUrl: String,
)
