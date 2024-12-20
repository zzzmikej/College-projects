package com.example.nimbus.domain

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.UUID

data class Training(
    val id: UUID,
    @SerializedName("inicialDateTime") val initialDateTime: LocalDateTime,
    val finalDateTime: LocalDateTime,
    val local: String?,
    val team: UUID
)