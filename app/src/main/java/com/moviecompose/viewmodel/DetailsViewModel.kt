package com.moviecompose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.moviecompose.model.Movie
import com.moviecompose.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(val repository: FilmRepository) : ViewModel() {
    private var _similarFilms = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val similarMovies: State<Flow<PagingData<Movie>>> = _similarFilms

    fun getSimilarFilms(filmId: Int) {
        viewModelScope.launch {
            repository.getSimilarFilms(filmId).also {
                _similarFilms.value = it
            }.cachedIn(viewModelScope)
        }
    }

}
