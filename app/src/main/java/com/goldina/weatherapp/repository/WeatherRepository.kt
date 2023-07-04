package com.goldina.weatherapp.repository

import com.goldina.weatherapp.api.WeatherApi
import com.goldina.weatherapp.utils.DataState
import com.goldina.weatherapp.utils.TypeData
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository
@Inject
constructor(private val weatherRetrofit: WeatherApi){
    suspend fun getResponse(lat:String,lon:String,appid:String,units: String,lang:String, type: TypeData): Flow<DataState<Any>> = flow {
        emit(DataState.Loading)
        val response =  if(type == TypeData.CURRENT_WEATHER)
            weatherRetrofit.getCurrentWeatherData(lat, lon, appid, units,lang)
        else  weatherRetrofit.getNextWeatherData(lat, lon, appid, units)
        response.suspendOnSuccess {
            emit(DataState.Success(data))
        }
        response.suspendOnError{
            when (statusCode){
                StatusCode.Unauthorized -> emit(DataState.OtherError("Missing valid credentials"))
                StatusCode.BadGateway -> emit(DataState.OtherError("Something went wrong"))
                StatusCode.GatewayTimeout -> emit(DataState.OtherError("Unable to fetch data, please try again"))
                else -> emit(DataState.OtherError(message()))
            }
        }
        response.suspendOnException {
            if (exception.message!!.contains("Unable to resolve host")) {
                emit(DataState.OtherError("Unable to process request, please try again later"))
            }else{
                emit(DataState.Error(exception))
            }

        }

    }

}