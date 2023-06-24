package com.goldina.weatherapp.model

import java.io.Serializable

data class WeatherNextDay(
    val date: String,
    val dayName: String,
    val tempMin: Double,
    val tempMax: Double,
    val listHourly: MutableList<DayWeather>
): Serializable