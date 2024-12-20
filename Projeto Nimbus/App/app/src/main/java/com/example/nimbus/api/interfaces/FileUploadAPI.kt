package com.example.nimbus.api.interfaces

import com.example.nimbus.domain.ApiResponse
import com.example.nimbus.dto.User.UserCreateDTO
import com.example.nimbus.dto.User.UserLoginDTO
import com.example.nimbus.dto.User.UserLoginResponseDTO
import com.example.nimbus.domain.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface FileUploadAPI {
    @Multipart
    @POST("/blob-storage/{entity}")
    suspend fun postImage(
        @Path("entity") entity: UUID,
        @Part file: MultipartBody.Part,
    ): Response<ApiResponse<UserLoginResponseDTO>>
}