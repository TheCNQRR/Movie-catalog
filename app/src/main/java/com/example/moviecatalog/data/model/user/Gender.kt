package com.example.moviecatalog.data.model.user

import kotlinx.serialization.Serializable

@Serializable
enum class Gender(val value: Int) {
    MALE(0),
    FEMALE(1)
}