package com.goldina.weatherapp.model.response

import com.goldina.weatherapp.model.Clouds
import com.goldina.weatherapp.model.Coord
import com.goldina.weatherapp.model.MainX
import com.goldina.weatherapp.model.RainX
import com.goldina.weatherapp.model.SysX
import com.goldina.weatherapp.model.Weather
import com.goldina.weatherapp.model.Wind
import java.io.Serializable

data class ResponseCurrentDate(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    @Transient
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: MainX,
    val name: String,
    val rain: RainX,
    val sys: SysX,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
) : Serializable