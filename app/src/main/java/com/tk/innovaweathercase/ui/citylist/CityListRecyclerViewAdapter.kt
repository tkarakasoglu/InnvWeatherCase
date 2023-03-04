package com.tk.innovaweathercase.ui.citylist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.databinding.ItemCityBinding

interface OnCityFavoritesChangeListener {
    fun onCityFavoritesChange(city: City, isFav: Boolean)
}

class CityListRecyclerViewAdapter(private val onFavoritesChangeListener: OnCityFavoritesChangeListener) : RecyclerView.Adapter<CityListRecyclerViewAdapter.CityViewHolder>() {
    private var cityMutableList = mutableListOf<City>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemBinding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return cityMutableList.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cityMutableList[position]
        holder.bind(city, onFavoritesChangeListener)
    }

    class CityViewHolder(private val binding: ItemCityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City, onFavoritesChangeListener: OnCityFavoritesChangeListener) {
            binding.cityItemCheckBox.text = city.toponymName
            binding.cityItemCheckBox.isChecked = city.isFav
            binding.cityItemCheckBox.setOnClickListener {
                onFavoritesChangeListener.onCityFavoritesChange(city, !city.isFav)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCityMutableList(cityList: List<City>) {
        this.cityMutableList = cityList.toMutableList()
        notifyDataSetChanged()
    }
}