package com.example.nimbus.domain

data class ApiResponse<T>(
    val clientMsg: String?,
    val serverMsg: String?,
    val data: T
)