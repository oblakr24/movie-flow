package com.rokoblak.flowtest.ui.main.movies

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [MovieEntity::class], version = 3, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val moviesDAO: MoviesDao

    companion object {

        fun create(context: Context)
                = Room.databaseBuilder(context, MoviesDatabase::class.java, "MoviesDatabase").build()
    }
}
