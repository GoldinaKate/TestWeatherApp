package com.goldina.weatherapp.di

import com.goldina.weatherapp.api.WeatherApi
import com.goldina.weatherapp.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL: String, gson: Gson): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WeatherApi::class.java)
    }
}