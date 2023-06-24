package com.goldina.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldina.weatherapp.model.response.ResponseCurrentDate
import com.goldina.weatherapp.model.response.ResponseNextDay
import com.goldina.weatherapp.repository.WeatherRepository
import com.goldina.weatherapp.utils.DataState
import com.goldina.weatherapp.utils.TypeData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject
constructor(private val repository: WeatherRepository) : ViewModel() {
    private val _respCurWeather = MutableLiveData<DataState<ResponseCurrentDate>>()
    val respCurWeather: LiveData<DataState<ResponseCurrentDate>>
        get() =_respCurWeather

    private val _respNextWeather = MutableLiveData<DataState<ResponseNextDay>>()
    val respNextWeather: LiveData<DataState<ResponseNextDay>>
        get() =_respNextWeather



    fun getCurrentWeatherData(lat:String,lon:String,appid:String,units: String)
    = viewModelScope.launch {
            repository.getResponse(lat, lon, appid, units, "ru",TypeData.CURRENT_WEATHER).onEach {
                _respCurWeather.value = it as DataState<ResponseCurrentDate>
            }.launchIn(viewModelScope)
    }
    fun getNextWeatherData(lat:String,lon:String,appid:String,units: String) = viewModelScope.launch {
        repository.getResponse(lat, lon, appid, units, "ru",TypeData.NEXT_WEATHER).onEach {
            _respNextWeather.value = it as DataState<ResponseNextDay>
        }.launchIn(viewModelScope)
    }


}
