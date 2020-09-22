package com.rokoblak.flowtest.ui.main.movies

import android.app.Application
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rokoblak.flowtest.ui.main.retrofit.ApiUtil
import kotlinx.coroutines.launch


class MoviesViewModel(app: Application) : AndroidViewModel(app) {

    private val api = ApiUtil.createInstance()
    private val db  = MoviesDatabase.create(getApplication())
    private val dataStore = getApplication<Application>().createDataStore(name = "settings")

    private val repo = MoviesRepository(dataStore, api, db.moviesDAO)

    val queried = repo.filteredEntities.asLiveData()

    fun updateQuery(query: String) = viewModelScope.launch {
        repo.fetch(query = query)
    }

    fun updateToggle(enabled: Boolean) = viewModelScope.launch {
        repo.updateOnlyNew(enabled)
    }

    fun setFavourite(movieEntity: MovieEntity) = viewModelScope.launch {
        repo.setFavourite(entity = movieEntity)
    }
}