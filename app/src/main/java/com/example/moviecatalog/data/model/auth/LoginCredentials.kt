package com.example.moviecatalog.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(
    val username: String? = null,
    val password: String? = null
)
