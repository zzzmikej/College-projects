package com.example.nimbus.utils

import com.example.nimbus.domain.Game
import com.example.nimbus.domain.InjurySeverities
import com.example.nimbus.dto.Game.GameWithTeamDTO
import com.example.nimbus.dto.Injury.InjuryGetDTO
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun calcMapAvg(map: Map<String, Int>): Double {
    return if(map.size > 0) map.values.sum().toDouble() / map.size else 0.0
}

fun List<Game>.getLastConfirmedGame(): Game? {
    return this
        .filter { game -> game.confirmed && game.gameResult != null && LocalDateTime.parse(game.finalDateTime) < LocalDateTime.now() }
        .maxByOrNull { LocalDateTime.parse(it.finalDateTime) }
}

fun List<Game>.getNextConfirmedGame(): Game? {
    return this
        .filter { game -> game.gameResult == null && LocalDateTime.parse(game.inicialDateTime) > LocalDateTime.now() }
        .minByOrNull { it.inicialDateTime }
}

fun List<Game>.getConfirmedGames(): List<Game>? {
    return this
        .filter { game -> game.confirmed }
}

fun List<InjuryGetDTO>.getSeverities(): InjurySeverities {
    var minor = 0
    var mid = 0
    var severe = 0

    this.forEach {
        when(it.getSeverity()) {
            "Leve" ->  minor++
            "Média" -> mid++
            "Grave" -> severe++
            else -> {}
        }
    }

    return InjurySeverities(minor, mid, severe)
}

fun List<InjuryGetDTO>.getLast6Months(): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val sixMonthsAgo = LocalDate.now().minusMonths(6)

    return this.count { injury ->
        val injuryDate = LocalDate.parse(injury.initialDate, formatter)
        injuryDate.isAfter(sixMonthsAgo) || injuryDate.isEqual(sixMonthsAgo)
    }
}

fun List<InjuryGetDTO>.getDaysInjuried(): Int {
    var daysOut = 0

    this.forEach {
        daysOut += it.getDaysInjuried()
    }

    return daysOut
}

fun List<GameWithTeamDTO>.sortByClosestDate(): List<GameWithTeamDTO> {
    val now = LocalDateTime.now()

    return this.sortedWith(compareBy<GameWithTeamDTO> {
        val gameDate = LocalDateTime.parse(it.inicialDateTime)
        if (gameDate.isAfter(now)) 0 else 1
    }.thenBy {
        // Ordenação pela proximidade com a data atual
        val gameDate = LocalDateTime.parse(it.inicialDateTime)
        kotlin.math.abs(java.time.Duration.between(now, gameDate).toMillis())
    })
}