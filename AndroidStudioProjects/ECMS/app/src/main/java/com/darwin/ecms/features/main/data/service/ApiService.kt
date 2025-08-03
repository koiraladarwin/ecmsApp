package com.darwin.ecms.features.main.data.service

import retrofit2.Response
import com.darwin.ecms.features.main.data.dto.CheckInRequest
import com.darwin.ecms.features.main.data.dto.EventInfoDto
import com.darwin.ecms.features.main.data.dto.EventsDataDto
import com.darwin.ecms.features.main.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("event")
    suspend fun getEvents(
        @Header("Authorization") token: String
    ): EventsDataDto

    @GET("eventinfo")
    suspend fun getEventInfo(
        @Query("event_id") eventId: String
    ): EventInfoDto

    @GET("users/{event_id}")
    suspend fun getUsersByEvent(@Path("event_id") eventId: String): UserDto

    @POST("checkins")
    suspend fun createCheckIn(
        @Body checkInRequest: CheckInRequest
    ): Response<CheckInRequest>
}
