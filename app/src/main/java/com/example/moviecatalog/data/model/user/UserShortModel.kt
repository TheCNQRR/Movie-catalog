package com.example.moviecatalog.data.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class UserShortModel(
    val userId: String,
    val nickName: String,
    val avatar: String?
): Parcelable
