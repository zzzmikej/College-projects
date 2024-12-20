package com.example.nimbus.domain

import java.time.LocalDate
import java.util.UUID

data class Coach(
    val id: UUID? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDate: String? = null,
    val phone: String? = null
)