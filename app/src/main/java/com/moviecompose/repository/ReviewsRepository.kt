package com.moviecompose.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.moviecompose.model.response.Review
import com.moviecompose.network.ApiService
import com.moviecompose.paging.ReviewsSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReviewsRepository @Inject constructor(private val api: ApiService) {

    fun getFilmReviews(filmId: Int): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                ReviewsSource(api = api, filmId = filmId)
            }
        ).flow
    }
}
