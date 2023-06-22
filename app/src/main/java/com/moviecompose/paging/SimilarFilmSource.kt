package com.moviecompose.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moviecompose.model.Movie
import com.moviecompose.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class SimilarFilmSource(
    private val api: ApiService,
    val filmId: Int,
) :
    PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key ?: 1
            val similarMovies = api.getSimilarMovies(
                page = nextPage, filmId = filmId
            )

            LoadResult.Page(
                data = similarMovies.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (similarMovies.results.isEmpty()) null else similarMovies.page + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}