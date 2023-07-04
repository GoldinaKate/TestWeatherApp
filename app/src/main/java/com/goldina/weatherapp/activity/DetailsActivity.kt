package com.goldina.weatherapp.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.goldina.weatherapp.R
import com.goldina.weatherapp.databinding.ActivityDetailsBinding
import com.goldina.weatherapp.model.response.ResponseCurrentDate
import com.goldina.weatherapp.utils.Constants
import com.goldina.weatherapp.utils.WeatherType
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)

        binding.layoutWeatherBasic.tvCityCountry.text=getString(R.string.city_name)
        val currentWeather =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(Constants.DETAIL_ITEM, ResponseCurrentDate::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra(Constants.DETAIL_ITEM) as ResponseCurrentDate
            }

        if (currentWeather != null){
            setData(currentWeather)
        }
            setContentView(binding.root)
    }

    private fun setData(currentWeather: ResponseCurrentDate) {
        val dayNameFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd MMM",Locale.getDefault())
        val timeCheck = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dayNameFormat.timeZone = TimeZone.getTimeZone("UTC")
        binding.apply {
            layoutWeatherBasic.ivWeatherCondition.setImageDrawable(
                ContextCompat.getDrawable(this@DetailsActivity,
                    WeatherType.fromWMO(currentWeather.weather[0].icon).iconRes))
            layoutWeatherBasic.tvTemperature.text=currentWeather.main.temp.toInt().toString()
            layoutWeatherBasic.tvWeatherCondition.text=currentWeather.weather[0].description
            val date= currentWeather.dt*1000
            layoutWeatherBasic.tvDateTime.text =
                getString(R.string.date,dayNameFormat.format(date), dateFormat.format(date))
            layoutSunsetSunrise.tvSunriseTime.text =
                timeCheck.format(currentWeather.sys.sunrise*1000)
            layoutSunsetSunrise.tvSunsetTime.text =
                timeCheck.format(currentWeather.sys.sunset*1000)
            layoutWeatherAdditional.tvHumidity.text = getString(R.string.percent,currentWeather.main.humidity)
            layoutWeatherAdditional.tvPressure.text = getString(R.string.pressure,currentWeather.main.pressure)
            layoutWeatherAdditional.tvWind.text = getString(R.string.wind,currentWeather.wind.speed.toInt())
            val rain =currentWeather.rain.one_h ?: 0.0
            tvRain.text =getString(R.string.rain,rain)
            tvClouds.text = getString(R.string.percent,currentWeather.clouds.all)
            tvVisibility.text = getString(R.string.visibility,currentWeather.visibility)
            tvTemperature.text =getString(R.string.temp_max_min,
                currentWeather.main.temp_max.toInt(),currentWeather.main.temp_min.toInt())
            tvFeelsLike.text=getString(R.string.temp,currentWeather.main.feels_like.toInt())
        }
    }
}