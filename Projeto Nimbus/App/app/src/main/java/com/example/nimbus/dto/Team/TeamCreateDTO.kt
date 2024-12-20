package com.example.nimbus.dto.Team

import com.example.nimbus.domain.Coach

data class TeamCreateDTO(
    val name: String,
    val category: String,
    val local: String,
    val coach: Coach
)