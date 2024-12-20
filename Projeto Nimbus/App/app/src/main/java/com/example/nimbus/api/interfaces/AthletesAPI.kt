package com.example.nimbus.api.interfaces

import com.example.nimbus.domain.ApiResponse
import com.example.nimbus.domain.Athlete
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface AthletesAPI {

    @GET("/athletes")
    suspend fun getAllAthletes(): Response<ApiResponse<List<Athlete>>>

    @GET("/athletes/{id}")
    suspend fun getAthlete(@Path("id") id: UUID): Response<ApiResponse<Athlete>>

    @GET("/athletes/by-team/{id}")
    suspend fun getAllByTeam(@Path("id") id: UUID): Response<ApiResponse<List<Athlete>>>
}