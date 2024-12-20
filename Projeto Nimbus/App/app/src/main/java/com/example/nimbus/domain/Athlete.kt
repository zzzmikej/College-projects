package com.example.nimbus.domain

import com.example.nimbus.dto.Injury.InjuryGetDTO
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Athlete(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val phone: String,
    val picture: String?,
    val category: String,
    val isStarting: Boolean,
    val isInjuried: Boolean,
    val athleteDesc: AthleteDesc?, //descomentar para a implementação com a API real
    val injuries: List<InjuryGetDTO>?, //descomentar para a implementação com a API real
) {
    fun getIdade(): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dtNascto = LocalDate.parse(this.birthDate, formatter)

        return Period.between(dtNascto, LocalDate.now()).years
    }

    fun getFormattedBirthDate(): String {
        val formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(this.birthDate, formatterIn)

        val formatterOut = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return date.format(formatterOut)
    }
}