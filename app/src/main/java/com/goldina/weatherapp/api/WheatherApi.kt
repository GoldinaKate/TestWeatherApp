package com.goldina.weatherapp.api

import com.goldina.weatherapp.model.response.ResponseCurrentDate
import com.goldina.weatherapp.model.response.ResponseNextDay
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
        @GET("weather")
        suspend fun getCurrentWeatherData(
                @Query("lat") lat:String,
                @Query("lon") lon:String,
                @Query("APPID") appid:String,
                @Query("units") units:String,
                @Query("lang") lang:String
        ): ApiResponse<ResponseCurrentDate>

        @GET("forecast")
        suspend fun getNextWeatherData(
                @Query("lat") lat:String,
                @Query("lon") lon:String,
                @Query("APPID") appid:String,
                @Query("units") units:String
        ): ApiResponse<ResponseNextDay>
}