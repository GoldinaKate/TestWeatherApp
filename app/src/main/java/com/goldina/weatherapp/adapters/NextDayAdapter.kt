package com.goldina.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goldina.weatherapp.R
import com.goldina.weatherapp.databinding.NextdayItemBinding
import com.goldina.weatherapp.model.WeatherNextDay

class NextDayAdapter(private val items: List<WeatherNextDay>) :
    RecyclerView.Adapter<NextDayAdapter.ViewHolder>() {

    private lateinit var adapter: HourlyAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NextdayItemBinding
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

    inner class ViewHolder(private val binding: NextdayItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherDay: WeatherNextDay) = with(itemView) {
            binding.tvMaxTemp.text = context.getString(R.string.temp_max,weatherDay.tempMax.toInt())
            binding.tvMinTemp.text = context.getString(R.string.temp_min,weatherDay.tempMin.toInt())
            binding.tvDate.text = context.getString(R.string.date,weatherDay.dayName,weatherDay.date)
            adapter = HourlyAdapter(weatherDay.listHourly)
            binding.rvWeatherHourly.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            binding.rvWeatherHourly.setHasFixedSize(true)
            binding.rvWeatherHourly.adapter = adapter
        }

    }
}