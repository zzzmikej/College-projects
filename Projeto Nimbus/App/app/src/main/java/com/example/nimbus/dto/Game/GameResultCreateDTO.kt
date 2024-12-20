package com.example.nimbus.dto.Game

data class GameResultCreateDTO(
    val challengerPoints: Int,
    val challengedPoints: Int,
    val game: GameIdDTO
)