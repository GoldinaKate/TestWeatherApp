package com.goldina.weatherapp.model.response

import com.goldina.weatherapp.model.City
import com.goldina.weatherapp.model.DayWeather
import java.io.Serializable

data class ResponseNextDay(
    @Transient
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<DayWeather>,
    val message: Int
):Serializable