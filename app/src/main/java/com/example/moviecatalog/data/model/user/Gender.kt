package com.example.moviecatalog.data.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Gender() {
    @SerialName("0")
    MALE,

    @SerialName("1")
    FEMALE

    // TODO Нужен ли данный класс?
}
