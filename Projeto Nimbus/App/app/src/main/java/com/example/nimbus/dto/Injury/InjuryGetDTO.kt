package com.example.nimbus.dto.Injury

import com.google.gson.annotations.SerializedName
import java.util.UUID
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class InjuryGetDTO(
    val id: UUID,
    val type: String,
    @SerializedName("inicialDate") val initialDate: String,
    val finalDate: String
) {
    fun getSeverity(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = LocalDate.parse(this.initialDate, formatter)
        val endDate = LocalDate.parse(this.finalDate, formatter)

        val diffDays = ChronoUnit.DAYS.between(startDate, endDate)

        return when {
            diffDays <= 7 -> "Leve"
            diffDays <= 30 -> "Média"
            else -> "Grave"
        }
    }

    fun getDaysInjuried(): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = LocalDate.parse(this.initialDate, formatter)
        val endDate = LocalDate.parse(this.finalDate, formatter)

        return ChronoUnit.DAYS.between(startDate, endDate).toInt()
    }
}

