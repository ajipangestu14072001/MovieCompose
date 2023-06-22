package com.moviecompose.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.moviecompose.model.Genre
import com.moviecompose.model.Movie
import com.moviecompose.repository.FilmRepository
import com.moviecompose.repository.GenreRepository
import com.moviecompose.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val filmRepository: FilmRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {
    private var _filmGenres = mutableStateListOf(Genre(null, ""))
    val filmGenres: SnapshotStateList<Genre> = _filmGenres

    var selectedGenre: MutableState<Genre> = mutableStateOf(Genre(null, ""))

    private var _trendingMovies = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val trendingMoviesState: State<Flow<PagingData<Movie>>> = _trendingMovies

    private var _popularFilms = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val popularFilmsState: State<Flow<PagingData<Movie>>> = _popularFilms

    private var _topRatedFilm = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val topRatedFilmState: State<Flow<PagingData<Movie>>> = _topRatedFilm

    private var _nowPlayingFilm = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val nowPlayingMoviesState: State<Flow<PagingData<Movie>>> = _nowPlayingFilm

    private var _upcomingFilms = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val upcomingMoviesState: State<Flow<PagingData<Movie>>> = _upcomingFilms

    private var _recommendedFilms = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val recommendedMovies: MutableState<Flow<PagingData<Movie>>> = _recommendedFilms

    init {
        refreshAll()
    }

    fun refreshAll(
        genreId: Int? = selectedGenre.value.id,
    ) {
        if (filmGenres.size == 1) {
            getFilmGenre()
        }
        if (genreId == null) {
            selectedGenre.value = Genre(null, "All")
        }
        getTrendingFilms(genreId)
        getPopularFilms(genreId)
        getTopRatedFilm(genreId)
        getNowPlayingFilms(genreId)
        getUpcomingFilms(genreId)
    }

    fun filterBySetSelectedGenre(genre: Genre) {
        selectedGenre.value = genre
        refreshAll(genre.id)
    }

    private fun getFilmGenre() {
        viewModelScope.launch {
            val defaultGenre = Genre(null, "All")
            when (val results = genreRepository.getMoviesGenre()) {
                is Resource.Success -> {
                    _filmGenres.clear()
                    _filmGenres.add(defaultGenre)
                    selectedGenre.value = defaultGenre
                    results.data?.genres?.forEach {
                        _filmGenres.add(it)
                    }
                }
                is Resource.Error -> {
                    Log.e("ERROR","Error loading Genres")
                }
                else -> { }
            }
        }
    }

    private fun getTrendingFilms(genreId: Int?) {
        viewModelScope.launch {
            _trendingMovies.value = if (genreId != null) {
                filmRepository.getTrendingFilms().map { results ->
                    results.filter { movie ->
                        movie.genreIds!!.contains(genreId)
                    }
                }.cachedIn(viewModelScope)
            } else {
                filmRepository.getTrendingFilms().cachedIn(viewModelScope)
            }
        }
    }

    private fun getPopularFilms(genreId: Int?) {
        viewModelScope.launch {
            _popularFilms.value = if (genreId != null) {
                filmRepository.getPopularFilms().map { results ->
                    results.filter { movie ->
                        movie.genreIds!!.contains(genreId)
                    }
                }.cachedIn(viewModelScope)
            } else {
                filmRepository.getPopularFilms().cachedIn(viewModelScope)
            }
        }
    }

    private fun getTopRatedFilm(genreId: Int?) {
        viewModelScope.launch {
            _topRatedFilm.value = if (genreId != null) {
                filmRepository.getTopRatedFilm().map { results ->
                    results.filter { movie ->
                        movie.genreIds!!.contains(genreId)
                    }
                }.cachedIn(viewModelScope)
            } else {
                filmRepository.getTopRatedFilm().cachedIn(viewModelScope)
            }
        }
    }

    private fun getNowPlayingFilms(genreId: Int?) {
        viewModelScope.launch {
            _nowPlayingFilm.value = if (genreId != null) {
                filmRepository.getNowPlayingFilms().map { results ->
                    results.filter { movie ->
                        movie.genreIds!!.contains(genreId)
                    }
                }.cachedIn(viewModelScope)
            } else {
                filmRepository.getNowPlayingFilms().cachedIn(viewModelScope)
            }
        }
    }


    private fun getUpcomingFilms(genreId: Int?) {
        viewModelScope.launch {
            _upcomingFilms.value = if (genreId != null) {
                filmRepository.getUpcomingTvShows().map { results ->
                    results.filter { movie ->
                        movie.genreIds!!.contains(genreId)
                    }
                }.cachedIn(viewModelScope)
            } else {
                filmRepository.getUpcomingTvShows().cachedIn(viewModelScope)
            }
        }
    }

}