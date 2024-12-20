package com.example.nimbus.api.interfaces

import com.example.nimbus.domain.ApiResponse
import com.example.nimbus.domain.Forecast
import com.example.nimbus.domain.Team
import com.example.nimbus.dto.Team.TeamCreateDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface TeamsAPI {
    @GET("/teams")
    //suspend fun getAllTeams(): Response<List<Team>>
    suspend fun getAllTeams(): Response<ApiResponse<List<Team>>>

    @GET("/teams/{id}")
    suspend fun getTeam(@Path("id") id: UUID): Response<ApiResponse<Team>>
    
    @GET("/teams/by-coach/{id}")
    suspend fun getAllByCoach(@Path("id") id: UUID): Response<ApiResponse<List<Team>>>

    @GET("/teams/generate-forecast/{challenger}/{challenged}")
    suspend fun generateForecast(
        @Path("challenger") challenger: UUID,
        @Path("challenged") challenged: UUID
    ): Response<ApiResponse<Forecast>>

    @POST("/teams")
    suspend fun postTeam(@Body team: TeamCreateDTO): Response<ApiResponse<Team>>
}