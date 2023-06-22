package com.moviecompose.repository

import com.moviecompose.model.response.GenreResponse
import com.moviecompose.network.ApiService
import com.moviecompose.utils.Resource
import java.lang.Exception
import javax.inject.Inject

class GenreRepository @Inject constructor(private val api: ApiService) {
    suspend fun getMoviesGenre(): Resource<GenreResponse>{
        val response = try {
            api.getMovieGenres()
        } catch (e: Exception){
            return Resource.Error("Unknown error occurred!")
        }
        return Resource.Success(response)
    }
}