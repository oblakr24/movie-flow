package com.rokoblak.flowtest.ui.main.movies

import android.app.Application
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rokoblak.flowtest.ui.main.movies.model.MainInputState
import com.rokoblak.flowtest.ui.main.retrofit.ApiUtil
import kotlinx.coroutines.launch


class MainViewModel(app: Application) : AndroidViewModel(app) {

    val inputState = MutableLiveData(MainInputState())

    private val repo = MoviesRepository(
            getApplication<Application>().createDataStore(name = "settings"),
            ApiUtil.createInstance(), MoviesDatabase.create(getApplication()).moviesDAO)

    val queried = repo.filteredEntities.asLiveData()

    fun init() = viewModelScope.launch {
        inputState.value = repo.getInitialState()
    }

    fun updateQuery(query: String) = viewModelScope.launch {
        repo.fetch(query = query)
    }

    fun updateToggle(enabled: Boolean) = viewModelScope.launch {
        repo.updateOnlyOld(enabled)
    }

    fun setFavourite(movieEntity: MovieEntity) = viewModelScope.launch {
        repo.setFavourite(entity = movieEntity)
    }
}