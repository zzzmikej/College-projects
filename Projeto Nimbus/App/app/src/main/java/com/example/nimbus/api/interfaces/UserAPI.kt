package com.example.nimbus.api.interfaces

import com.example.nimbus.domain.ApiResponse
import com.example.nimbus.dto.User.UserCreateDTO
import com.example.nimbus.dto.User.UserLoginDTO
import com.example.nimbus.dto.User.UserLoginResponseDTO
import com.example.nimbus.domain.User
import com.example.nimbus.dto.User.UserIdsDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface UserAPI {
    @GET("/users/{id}")
    suspend fun getById(@Path("id") id: UUID): Response<ApiResponse<User>>

    @POST("/users")
    suspend fun post(@Body userCreateDTO: UserCreateDTO): Response<ApiResponse<UserIdsDTO>>

    @POST("/users/login")
    suspend fun login(@Body userLoginDTO: UserLoginDTO): Response<ApiResponse<UserLoginResponseDTO>>
}