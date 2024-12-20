package com.example.nimbus.dto.User

import java.util.UUID

data class UserLoginResponseDTO(
    val userId: UUID,
    val username: String,
    val email: String?,
    val token: String,
    val personaId: UUID
)