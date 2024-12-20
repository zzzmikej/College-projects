package com.example.nimbus.dto.Game

import com.example.nimbus.domain.GameResult

data class LastGameDTO(
    val adversaryName: String,
    val gameResult: GameResult,

)