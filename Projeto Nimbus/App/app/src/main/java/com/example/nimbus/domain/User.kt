package com.example.nimbus.domain

import java.util.UUID

data class User(
    val id: UUID,
    val email: String,
    val password: String,
    val athlete: Athlete?,
    val coach: Coach?
)