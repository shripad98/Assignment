package com.myapplication.utils

import com.myapplication.model.MovieDetailModel
import com.myapplication.model.MovieListModel
import com.myapplication.model.UserListResponse
import com.myapplication.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APiInterface {
    @GET(".")
    fun getMovieList(@Query("apikey") key: String?, @Query("s") title : String?, @Query("type") type : String?): Call<MovieListModel>

    @GET(".")
    fun getMovieDetail(@Query("apikey") key: String?, @Query("i") title : String?): Call<MovieDetailModel>
}