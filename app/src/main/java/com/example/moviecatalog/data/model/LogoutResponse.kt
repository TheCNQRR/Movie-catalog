package com.example.moviecatalog.data.model

import kotlinx.serialization.Serializable

@Serializable
class LogoutResponse(
    val token: String = "",
    val message: String = "Logged out"
)
