package com.example.moviecatalog.data.model.auth

import com.example.moviecatalog.data.model.user.Gender
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterModel(
    val userName: String,
    val name: String,
    val password: String,
    val email: String,
    val birthDate: String?,
    val gender: Gender?
)
