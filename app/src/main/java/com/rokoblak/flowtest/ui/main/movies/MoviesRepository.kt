package com.rokoblak.flowtest.ui.main.movies

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.rokoblak.flowtest.ui.main.movies.model.MainInputState
import com.rokoblak.flowtest.ui.main.retrofit.OMDBApi
import kotlinx.coroutines.flow.*

class MoviesRepository(private val store: DataStore<Preferences>, private val api: OMDBApi, private val dao: MoviesDao) {

    private val queries = store.data.map { it[KEY_QUERY]?.toLowerCase() ?: "" }
            .distinctUntilChanged()

    private val onlyOldEnabled = store.data.map { it[KEY_ONLY_OLD] ?: false }
            .distinctUntilChanged()

    val filteredEntities = queries.flatMapLatest { query ->
        dao.getAllMoviesByQuery(query).distinctUntilChanged()
                .combine(onlyOldEnabled) { movies, onlyOld ->
            if (onlyOld) {
                movies.filterNot { it.isNew() }
            } else {
                movies
            }
        }
    }

    suspend fun getInitialState(): MainInputState {
        val query = queries.take(1).first()
        val newEnabled = onlyOldEnabled.take(1).first()
        return MainInputState(query, newEnabled)
    }

    suspend fun updateOnlyOld(enabled: Boolean) {
        store.edit { it[KEY_ONLY_OLD] = enabled }
    }

    suspend fun fetch(query: String) {
        store.edit { it[KEY_QUERY] = query }

        val models = try {
            api.searchMovies(query).Search ?: return
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        models.map { MovieEntity.from(it, query) }.takeIf { it.isNotEmpty() }?.let {
            dao.insertMovies(it)
        }
    }

    suspend fun setFavourite(entity: MovieEntity) {
        dao.setFavourite(entity.imdbID, !entity.favourite)
    }

    companion object {
        private val KEY_QUERY = preferencesKey<String>("key-query")
        private val KEY_ONLY_OLD = preferencesKey<Boolean>("key-only-new-enabled")
    }
}