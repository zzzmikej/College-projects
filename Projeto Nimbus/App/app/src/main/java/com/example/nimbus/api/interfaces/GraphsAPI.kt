package com.example.nimbus.api.interfaces

import com.example.nimbus.domain.ApiResponse
import com.example.nimbus.dto.Graphs.PointsDivisionDTO
import com.example.nimbus.dto.Graphs.WinsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface GraphsAPI {
    @GET("/graphs/points-per-game/{id}")
    suspend fun getPointsPerGame(
        @Path("id") id: UUID,
        @Query("matches") matches : Int = 20
    ): Response<ApiResponse<Map<String, Int>>>

    @GET("/graphs/wins-by-team-matches/{id}")
    suspend fun getWinsByTeam(
        @Path("id") id: UUID,
        @Query("matches") matches: Int = 20
    ): Response<ApiResponse<WinsDTO>>

    @GET("/graphs/points-division-per-team/{id}")
    suspend fun getPointsDivision(
        @Path("id") id: UUID,
        @Query("matches") matches: Int = 20
    ): Response<ApiResponse<PointsDivisionDTO>>

    @GET("/graphs/fouls-per-game/{id}")
    suspend fun getFoulsPerGame(
        @Path("id") id: UUID,
        @Query("matches") matches: Int = 20
    ): Response<ApiResponse<Map<String, Int>>>

    //@GET
    //suspend fun getReboundsPerGame(
    //    @Path("id") id: UUID,
    //    @Query("matches") matches: Int = 20
    //)
}