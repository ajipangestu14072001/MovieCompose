package com.moviecompose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.moviecompose.model.response.Review
import com.moviecompose.repository.ReviewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(private val repo: ReviewsRepository) : ViewModel() {

    private val _filmReviews = mutableStateOf<Flow<PagingData<Review>>>(emptyFlow())
    val filmReviews: State<Flow<PagingData<Review>>> = _filmReviews

    fun getFilmReview(filmId: Int) {
        viewModelScope.launch {
            _filmReviews.value = repo.getFilmReviews(filmId = filmId).cachedIn(viewModelScope)
        }
    }
}
