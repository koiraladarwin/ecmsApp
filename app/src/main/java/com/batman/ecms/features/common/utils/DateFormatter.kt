package com.batman.ecms.features.common.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

private val outputFormatter = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).apply {
    timeZone = TimeZone.getDefault()
}

fun formatDate(isoDate: String): String {
    return try {
        val date: Date = isoFormatter.parse(isoDate)!!
        outputFormatter.format(date)
    } catch (e: ParseException) {
        "failed"
    }
}
