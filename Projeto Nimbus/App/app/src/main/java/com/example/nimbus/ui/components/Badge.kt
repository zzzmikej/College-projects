package com.example.nimbus.ui.components

import com.example.nimbus.R

data class Badge(
    val icon: Int,
    val text: String
)

fun getBadge(level: Int): Badge {
    val badges = listOf(
        Badge(R.drawable.badge_1, "Destinado"),
        Badge(R.drawable.badge_2, "Desafiante"),
        Badge(R.drawable.badge_3, "Conquistador"),
        Badge(R.drawable.badge_4, "Rei da quadra")
    )

    return when(level) {
        2 -> badges[1]
        3 -> badges[2]
        4 -> badges[3]
        else -> badges[0]
    }
}