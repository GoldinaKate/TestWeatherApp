package com.goldina.weatherapp.utils

import androidx.annotation.DrawableRes
import com.goldina.weatherapp.R

sealed class WeatherType (
@DrawableRes val iconRes: Int
) {
    object Thunderstorm : WeatherType(
        iconRes = R.drawable.ic_thunderstorm
    )
    object ShowerRain : WeatherType(
        iconRes = R.drawable.ic_shower_rain
    )
    object RainDay : WeatherType(

        iconRes = R.drawable.ic_rain_day
    )
    object RainNight : WeatherType(
        iconRes = R.drawable.ic_rain_night
    )
    object Snow : WeatherType(
        iconRes = R.drawable.ic_snow
    )
    object Mist : WeatherType(
        iconRes = R.drawable.ic_mist
    )
    object ClearDay : WeatherType(
        iconRes = R.drawable.ic_day
    )
    object ClearNight : WeatherType(
        iconRes = R.drawable.ic_night
    )
    object FewCloudsDay : WeatherType(
        iconRes = R.drawable.ic_cloud_day
    )
    object FewCloudsNight : WeatherType(
        iconRes = R.drawable.ic_cloud_night
    )
    object Clouds : WeatherType(
        iconRes = R.drawable.ic_cloud
    )
    object BrokenClouds : WeatherType(
        iconRes = R.drawable.ic_broken_clouds
    )


    companion object {
        fun fromWMO(code: String): WeatherType {
            return when(code) {
                "11n","11d" -> Thunderstorm
                "13n","13d" -> Snow
                "50n","50d" -> Mist
                "09n","09d" -> ShowerRain
                "04n","04d" -> BrokenClouds
                "03n","03d" -> Clouds
                "01d" -> ClearDay
                "01n" -> ClearNight
                "02d" -> FewCloudsDay
                "02n" -> FewCloudsNight
                "10d" -> RainDay
                "10n" -> RainNight
                else -> {ClearDay}
            }
        }
    }
}
