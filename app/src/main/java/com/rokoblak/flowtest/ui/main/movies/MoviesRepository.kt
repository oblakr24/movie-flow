package com.rokoblak.flowtest.ui.main.movies

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.rokoblak.flowtest.ui.main.retrofit.OMDBApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map


class MoviesRepository(private val dataStore: DataStore<Preferences>, private val api: OMDBApi, private val dao: MoviesDao) {

    private val queriesFlow = dataStore.data.map { it[KEY_QUERY] ?: "" }

    private val onlyNewEnabledFlow = dataStore.data.map { it[KEY_ONLY_NEW_ENABLED] ?: false }

    val filteredEntities = queriesFlow.flatMapLatest { query ->
        dao.getAllMoviesByQuery(query).distinctUntilChanged().combine(onlyNewEnabledFlow) { movies, onlyNew ->
            if (onlyNew) {
                movies.filter { it.isNew() }
            } else {
                movies
            }
        }
    }

    suspend fun updateOnlyNew(enabled: Boolean) {
        dataStore.edit { it[KEY_ONLY_NEW_ENABLED] = enabled }
    }

    suspend fun fetch(query: String) {
        dataStore.edit { it[KEY_QUERY] = query }

        val models = try {
            api.searchMovies(query).Search
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        models?.map { MovieEntity.from(it, query) }?.takeIf { it.isNotEmpty() }?.let {
            dao.insertMovies(it)
        }
    }

    suspend fun setFavourite(entity: MovieEntity) {
        dao.setFavourite(entity.imdbID, !entity.favourite)
    }

    companion object {
        private val KEY_QUERY = preferencesKey<String>("key-query")
        private val KEY_ONLY_NEW_ENABLED = preferencesKey<Boolean>("key-only-new-enabled")
    }
}