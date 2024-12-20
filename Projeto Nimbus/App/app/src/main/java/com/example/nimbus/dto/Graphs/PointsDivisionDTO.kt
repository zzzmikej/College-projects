package com.example.nimbus.dto.Graphs

import com.google.gson.annotations.SerializedName

data class PointsDivisionDTO(
    @SerializedName("twoPointsPorcentage") val twoPoints: Double,
    @SerializedName("threePointsPorcentage") val threePoints: Double
)