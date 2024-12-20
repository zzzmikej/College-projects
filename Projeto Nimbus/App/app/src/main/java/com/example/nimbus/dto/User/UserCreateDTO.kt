package com.example.nimbus.dto.User

import com.example.nimbus.domain.Athlete
import com.example.nimbus.domain.Coach

data class UserCreateDTO(
    val email: String?,
    val password: String?,
    val coach: Coach?
)