package com.batman.ecms.features.main.data.service

import com.batman.ecms.features.main.data.dto.CheckInDto
import retrofit2.Response
import com.batman.ecms.features.main.data.dto.CheckInRequest
import com.batman.ecms.features.main.data.dto.EditRoleDto
import com.batman.ecms.features.main.data.dto.EventInfoDto
import com.batman.ecms.features.main.data.dto.ActivityRequest
import com.batman.ecms.features.main.data.dto.EventsDataDto
import com.batman.ecms.features.main.data.dto.StaffDto
import com.batman.ecms.features.main.data.dto.UserDto
import com.batman.ecms.features.main.data.dto.UserRequest
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
    ): Response<EventsDataDto>

    @GET("eventinfo")
    suspend fun getEventInfo(
        @Header("Authorization") token: String,
        @Query("event_id") eventId: String
    ): Response<EventInfoDto>

    @POST("addeventwithcode/{code}")
    suspend fun addEventWithCode(
        @Header("Authorization") token: String,
        @Path("code") code: String
    ): Response<Unit>

    @POST("modifyRoleToStaffs")
    suspend fun modifyStaffRole(
        @Header("Authorization") token: String,
        @Body editRoleDto: EditRoleDto
    ): Response<Unit>

    @GET("users/{event_id}")
    suspend fun getUsersByEvent(
        @Header("Authorization") token: String,
        @Path("event_id") eventId: String
    ): Response<UserDto>

    @POST("checkins")
    suspend fun createCheckIn(
        @Header("Authorization") token: String,
        @Body checkInRequest: CheckInRequest
    ): Response<Unit>

    @GET("activitycheckins/{activity_id}")
    suspend fun getCheckInByActivity(
        @Header("Authorization") token: String,
        @Path("activity_id") activityId: String
    ): Response<CheckInDto>

    @GET("attendeecheckins/{attendee_id}")
    suspend fun getCheckInByAttendee(
        @Header("Authorization") token: String,
        @Path("attendee_id") attendeeId: String
    ): Response<CheckInDto>

    @GET("getstaffs/{event_id}")
    suspend fun getStaffsByEventId(
        @Header("Authorization") token: String,
        @Path("event_id") eventId: String
    ): Response<List<StaffDto>>

    @POST("user")
    suspend fun addUser(
        @Header("Authorization") token: String,
        @Body user: UserRequest
    ): Response<Unit>

    @POST("event")
    suspend fun addActivity(
        @Header("Authorization") token: String,
        @Body user: ActivityRequest
    ): Response<Unit>
}
