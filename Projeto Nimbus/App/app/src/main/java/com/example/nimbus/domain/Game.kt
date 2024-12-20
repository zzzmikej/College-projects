package com.example.nimbus.domain

import java.time.format.DateTimeFormatter
import java.util.UUID

data class Game(
    val id: UUID,
    val confirmed: Boolean,
    val inicialDateTime: String,
    val finalDateTime: String,
    val local: String?,
    val challenger: UUID,
    val challenged: UUID,
    val gameResult: GameResult?
) {
    fun getAdversary(teamId: UUID): UUID {
        return if(teamId == challenger) challenged else challenger
    }

    fun getGameDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
        return this.inicialDateTime.format(formatter)
    }
}