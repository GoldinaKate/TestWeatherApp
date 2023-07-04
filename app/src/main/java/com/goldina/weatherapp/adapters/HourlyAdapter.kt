package com.goldina.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.goldina.weatherapp.R
import com.goldina.weatherapp.databinding.HourlyItemBinding
import com.goldina.weatherapp.model.DayWeather
import com.goldina.weatherapp.utils.WeatherType
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class HourlyAdapter(private val items: List<DayWeather>) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = HourlyItemBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }

        inner class ViewHolder(private val binding: HourlyItemBinding) : RecyclerView.ViewHolder(binding.root) {
            private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            fun bind(dayWeather: DayWeather) = with(itemView) {
                binding.tvTemp.text = context.getString(R.string.temp,dayWeather.main.temp.toInt())
                binding.icWeatherHourly.setImageDrawable(ContextCompat.getDrawable(context,
                WeatherType.fromWMO(dayWeather.weather[0].icon).iconRes))
                timeFormat.timeZone= TimeZone.getTimeZone("UTC")
                binding.tvTime.text = timeFormat.format(dayWeather.dt*1000)
            }

        }
    }
