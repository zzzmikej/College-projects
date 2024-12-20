package com.example.nimbus.api.interfaces

import com.example.nimbus.domain.ApiResponse
import com.example.nimbus.domain.Team
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface TrainingAPI {
    @GET("/trainings/{id}")
    suspend fun getTrainingsByTEam(@Path("id") id: UUID): Response<ApiResponse<List<Team>>>
}