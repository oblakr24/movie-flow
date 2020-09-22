package com.rokoblak.flowtest.ui.main.retrofit.model



data class MoviesResponse(
    val Search: List<Movie>?
) {
    data class Movie(
        val Title: String,
        val Year: String,
        val imdbID: String
    )
}