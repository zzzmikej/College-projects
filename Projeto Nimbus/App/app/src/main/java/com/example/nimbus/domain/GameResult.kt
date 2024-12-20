package com.example.nimbus.domain

data class GameResult(
    val id: String,
    val challengerPoints: Int,
    val chalengedPoints: Int,
    val confirmed: Boolean
)