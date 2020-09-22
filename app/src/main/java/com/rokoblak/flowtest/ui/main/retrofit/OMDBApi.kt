package com.rokoblak.flowtest.ui.main.retrofit

import com.rokoblak.flowtest.ui.main.retrofit.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface OMDBApi {

    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("apikey") apikey: String = ApiUtil.API_KEY
    ): MoviesResponse
}