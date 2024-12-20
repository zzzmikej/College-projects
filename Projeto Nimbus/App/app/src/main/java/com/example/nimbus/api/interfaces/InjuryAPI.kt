package com.example.nimbus.api.interfaces

import com.example.nimbus.domain.ApiResponse
import com.example.nimbus.domain.Injury
import com.example.nimbus.dto.User.UserCreateDTO
import com.example.nimbus.dto.User.UserLoginDTO
import com.example.nimbus.dto.User.UserLoginResponseDTO
import com.example.nimbus.domain.User
import com.example.nimbus.dto.Injury.InjuryCreateDTO
import com.example.nimbus.dto.Injury.InjuryGetDTO
import com.example.nimbus.dto.User.UserIdsDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface InjuryAPI {
    @POST("/injuries")
    suspend fun postInjury(@Body injuryCreateDTO: InjuryCreateDTO): Response<ApiResponse<InjuryGetDTO>>
}