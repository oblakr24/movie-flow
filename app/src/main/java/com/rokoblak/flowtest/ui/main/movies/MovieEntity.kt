package com.rokoblak.flowtest.ui.main.movies

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.rokoblak.flowtest.ui.main.retrofit.model.MoviesResponse


@Entity
data class MovieEntity(
    val title: String,
    val year: String,
    val orgQuery: String,
    val favourite: Boolean = false,
    @PrimaryKey val imdbID: String
) {

    @Ignore
    fun getParsedYear() = year.toIntOrNull() ?: year.take(4).toIntOrNull()

    fun isNew() = getParsedYear()?.let { it >= THRESHOLD_YEAR_NEW } ?: false

    companion object {

        private const val THRESHOLD_YEAR_NEW = 2010

        fun from(movie: MoviesResponse.Movie, orgQuery: String) =  MovieEntity(
            title = movie.Title,
            year = movie.Year,
            imdbID = movie.imdbID,
            orgQuery = orgQuery
        )
    }
}