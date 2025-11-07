package com.example.moviecatalog.logic.util

import com.example.moviecatalog.data.model.movie.MovieElementModel

class Functions {
    fun calculateMovieRating(movie: MovieElementModel): Double {
        val reviews = movie.reviews
        if (reviews.isEmpty()) {
            return  0.0
        }

        val rating: Double
        var reviewsSum = 0

        reviews.forEach { review ->
            reviewsSum += review.rating
        }

        rating = reviewsSum.toDouble() / reviews.size

        return rating
    }

    fun formatNumber(number: Int): String {
        val numberString = number.toString()
        val result = StringBuilder()

        for ((count, i) in (numberString.length - 1 downTo 0).withIndex()) {
            if (count > 0 && count % 3 == 0) {
                result.append(' ')
            }
            result.append(numberString[i])
        }

        return "$${result.reverse()}"
    }
}