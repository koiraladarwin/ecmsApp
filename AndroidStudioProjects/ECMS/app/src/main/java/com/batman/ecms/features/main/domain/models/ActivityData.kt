package com.batman.ecms.features.main.domain.models

data class ActivityData(
    val activityId:String,
    val title: String,
    val startTime: String,
    val endTime: String,
    val checkedIn: Int,
    val total: Int
) {
    val percentage: Float
        get() = if (total > 0) checkedIn.toFloat() / total else 0f
}
