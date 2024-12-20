package com.example.nimbus.domain

import java.util.UUID

data class Team(
    val id: UUID,
    val name: String,
    val category: String?,
    val picture: String?,
    val local: String?,
    val athletes: List<Athlete>?,
    val level: Int
)