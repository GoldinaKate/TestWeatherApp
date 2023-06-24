package com.goldina.weatherapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.goldina.weatherapp.R
import com.goldina.weatherapp.databinding.ActivityDetailsBinding
import com.goldina.weatherapp.model.response.ResponseCurrentDate
import com.goldina.weatherapp.utils.Constants
import com.goldina.weatherapp.utils.WeatherType
import java.text.SimpleDateFormat
import java.util.TimeZone

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)

        binding.layoutWeatherBasic.tvCityCountry.text=getString(R.string.city_name)
        val currentWeather =
            intent.getSerializableExtra(Constants.DETAIL_ITEM) as? ResponseCurrentDate

        if (currentWeather != null){
            setData(currentWeather)
        }
            setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setData(currentWeather: ResponseCurrentDate) {
        val dayNameFormat = SimpleDateFormat("EEEE")
        val dateFormat = SimpleDateFormat("dd MMM")
        val timeCheck = SimpleDateFormat("HH:mm")
        timeCheck.timeZone = TimeZone.getTimeZone("UTC")
        binding.apply {
            layoutWeatherBasic.ivWeatherCondition.setImageDrawable(
                ContextCompat.getDrawable(this@DetailsActivity,
                    WeatherType.fromWMO(currentWeather.weather[0].icon).iconRes))
            layoutWeatherBasic.tvTemperature.text=currentWeather.main.temp.toInt().toString()
            layoutWeatherBasic.tvWeatherCondition.text=currentWeather.weather[0].description
            val date= currentWeather.dt*1000
            layoutWeatherBasic.tvDateTime.text =
                "${dayNameFormat.format(date)}, ${dateFormat.format(date)}"
            layoutSunsetSunrise.tvSunriseTime.text =
                timeCheck.format(currentWeather.sys.sunrise*1000)
            layoutSunsetSunrise.tvSunsetTime.text =
                timeCheck.format(currentWeather.sys.sunset*1000)
            layoutWeatherAdditional.tvHumidity.text = "${currentWeather.main.humidity}%"
            layoutWeatherAdditional.tvPressure.text = "${currentWeather.main.pressure} гПа"
            layoutWeatherAdditional.tvWind.text = "${currentWeather.wind.speed.toInt()} м/с"
            tvRain.text = "1 час: ${currentWeather.rain} мм"
            tvClouds.text = "${currentWeather.clouds.all}%"
            tvVisibility.text =  "${currentWeather.visibility} м"
            tvTemperature.text =
                "Макс. ${currentWeather.main.temp_max.toInt()}°C\nМин. ${currentWeather.main.temp_min.toInt()}°C"
            tvFeelsLike.text="${currentWeather.main.feels_like.toInt()}°C"
        }
    }
}