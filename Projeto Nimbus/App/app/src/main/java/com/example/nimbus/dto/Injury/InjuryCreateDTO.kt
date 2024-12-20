package com.example.nimbus.dto.Injury

import com.example.nimbus.dto.Athlete.AthleteIdDTO
import java.time.LocalDate

data class InjuryCreateDTO(
    val inicialDate: String,
    val finalDate: String,
    val type: String,
    val athlete: AthleteIdDTO
)