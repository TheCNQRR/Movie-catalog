package com.example.moviecatalog.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalog.R
import com.example.moviecatalog.data.model.movie.MovieElementModel
import com.example.moviecatalog.databinding.MovieItemInGalleryBinding
import com.example.moviecatalog.logic.util.Functions
import com.squareup.picasso.Picasso
import java.util.Locale

class GalleryAdapter(
    private var movies: List<MovieElementModel> = emptyList(),
    private val onMovieClick: (MovieElementModel) -> Unit = {}
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    companion object {
        private const val RATING_MAX = 10.0
        private const val RATING_MASTERPIECE = 9.0
        private const val RATING_EXCELLENT = 8.0
        private const val RATING_VERY_GOOD = 7.0
        private const val RATING_GOOD = 6.0
        private const val RATING_DECENT = 5.0
        private const val RATING_AVERAGE = 4.0
        private const val RATING_BELOW_AVERAGE = 3.0
        private const val RATING_POOR = 2.0
        private const val RATING_VERY_POOR = 1.0
        private const val RATING_AWFUL = 0.0
    }

    class GalleryViewHolder(private val binding: MovieItemInGalleryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieElementModel, onMovieClick: (MovieElementModel) -> Unit) {
            setupMovieContent(movie)
            setupRating(movie)
            setupClickListeners(movie, onMovieClick)
        }

        private fun setupMovieContent(movie: MovieElementModel) {
            with(binding) {
                movieNameMainScreen.isSelected = true
                movieYearCountryMainScreen.isSelected = true
                movieGenresMainScreen.isSelected = true

                Picasso.get().load(movie.poster).into(moviePoster)
                movieNameMainScreen.text = movie.name
                movieYearCountryMainScreen.text = getYearCountryText(movie)
                movieGenresMainScreen.text = getGenresText(movie)
            }
        }

        private fun setupRating(movie: MovieElementModel) {
            with(binding) {
                val movieRating = Functions().calculateMovieRating(movie)
                val ratingBackground = getRatingBackground(movieRating)

                movieRatingMainScreen.text = String.format(Locale.getDefault(), "%.1f", movieRating)
                movieRatingMainScreen.setBackgroundResource(ratingBackground)
            }
        }

        private fun setupClickListeners(movie: MovieElementModel, onMovieClick: (MovieElementModel) -> Unit) {
            binding.cardView.setOnClickListener {
                onMovieClick(movie)
            }
        }

        private fun getYearCountryText(movie: MovieElementModel): String {
            return binding.root.context.getString(R.string.year_country_format, movie.year, movie.country)
        }

        private fun getGenresText(movie: MovieElementModel): String {
            return movie.genres.joinToString { it.name }
        }

        private fun getRatingBackground(rating: Double): Int {
            return when {
                rating in RATING_MASTERPIECE..RATING_MAX -> R.drawable.rating_masterprice
                rating in RATING_EXCELLENT..RATING_MASTERPIECE -> R.drawable.rating_excellent
                rating in RATING_VERY_GOOD..RATING_EXCELLENT -> R.drawable.rating_very_good
                rating in RATING_GOOD..RATING_VERY_GOOD -> R.drawable.rating_good
                rating in RATING_DECENT..RATING_GOOD -> R.drawable.rating_decent
                rating in RATING_AVERAGE..RATING_DECENT -> R.drawable.rating_average
                rating in RATING_BELOW_AVERAGE..RATING_AVERAGE -> R.drawable.rating_below_average
                rating in RATING_POOR..RATING_BELOW_AVERAGE -> R.drawable.rating_poor
                rating in RATING_VERY_POOR..RATING_POOR -> R.drawable.rating_very_poor
                rating in RATING_AWFUL..RATING_VERY_POOR -> R.drawable.rating_awful
                else -> R.drawable.rating_gray
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = MovieItemInGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GalleryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(movies[position], onMovieClick)
    }

    fun addMovies(newMovies: List<MovieElementModel>) {
        val startPosition = movies.size
        movies = movies + newMovies
        notifyItemRangeInserted(startPosition, newMovies.size)
    }
}
