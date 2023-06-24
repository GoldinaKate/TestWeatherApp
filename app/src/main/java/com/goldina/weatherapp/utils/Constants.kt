package com.goldina.weatherapp.utils

object Constants {
    const val API_KEY: String = "d9e6fe2ca9bd114df14262b014663852"
    const val BASE_URL="https://api.openweathermap.org/data/2.5/"
    const val LAT = "59.8944"
    const val LON = "30.2642"
    const val UNITS = "metric"
    const val DETAIL_ITEM = "detail_item"
}

enum class TypeData{
    CURRENT_WEATHER, NEXT_WEATHER
}