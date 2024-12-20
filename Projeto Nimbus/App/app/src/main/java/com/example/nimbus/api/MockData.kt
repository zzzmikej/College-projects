package com.example.nimbus.api

import com.example.nimbus.domain.Game
import com.example.nimbus.domain.GameResult
import com.example.nimbus.domain.Injury
import com.example.nimbus.domain.Team
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

//jogos
val game1 = Game(
    UUID.randomUUID(),
    true,
    "",
    "",
    "Rua Haddock Lobo",
    UUID.randomUUID(),
    UUID.randomUUID(),
    gameResult = null
)