package com.example.moviecatalog.data.model

object TokenStorage {
    var token: String = ""
        private set

    fun saveToken(newToken: String) {
        token = newToken
    }



    fun clearToken() {
        token = ""
    }
}