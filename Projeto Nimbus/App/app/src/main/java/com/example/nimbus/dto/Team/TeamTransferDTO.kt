package com.example.nimbus.dto.Team

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class TeamTransferDTO(
    val id: UUID,
    val name: String,
    val category: String?,
    val picture: String?,
    val local: String?,
    val level: Int
): Parcelable