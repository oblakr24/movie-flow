package com.rokoblak.flowtest.ui.main.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertMovies(entites: List<MovieEntity>)

    @Query("UPDATE MovieEntity SET favourite = :favourite WHERE imdbID = :id")
    abstract suspend fun setFavourite(id: String, favourite: Boolean)

    @Query("SELECT * FROM MovieEntity WHERE title GLOB '*' || :query|| '*' OR orgQuery = :query")
    abstract fun getAllMoviesByQuery(query: String): Flow<List<MovieEntity>>
}